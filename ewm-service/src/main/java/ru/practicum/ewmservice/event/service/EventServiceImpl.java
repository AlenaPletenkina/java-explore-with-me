package ru.practicum.ewmservice.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.BadDataException;
import ru.practicum.ewmservice.exception.BadRequestDataException;
import ru.practicum.ewmservice.exception.UserNotFoundException;
import ru.practicum.ewmservice.request.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.request.dto.RequestStatus;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.request.repository.RequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static ru.practicum.ewmservice.event.dto.AdminStateAction.PUBLISH_EVENT;
import static ru.practicum.ewmservice.event.dto.State.*;
import static ru.practicum.ewmservice.event.dto.StateAction.SEND_TO_REVIEW;
import static ru.practicum.ewmservice.request.dto.RequestStatus.CONFIRMED;

@Service
@ComponentScan(basePackages = {"ru.practicum.client"})  // для Idea
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final BaseClient baseClient;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<EventFullDto> getAllEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<Event> events = eventRepository.getEventsWithFilter(users, states, categories, rangeStart, rangeEnd);
        return events.stream()
                .skip(from)
                .limit(size)
                .map(EventMapper::toEventFullDto)
                .toList();
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        log.warn("Статус события: {}", event.getState());
        handleAdminRequestData(updateEventAdminRequest, event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getPublicEvents(HttpServletRequest httpRequest, String text, List<Integer> categories,
                                               Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size) {
        if (isNull(rangeStart)) {
            rangeStart = String.valueOf(LocalDateTime.now());
        }
        List<Event> events = eventRepository.getPublicEventsWithFilter(text.toLowerCase(), categories, paid, rangeStart,
                rangeEnd);
        sendStatistic(httpRequest);
        return events.stream()
                .filter(event -> {
                    if (onlyAvailable) {
                        long count = event.getRequests().stream()
                                .filter(request -> request.getStatus().equals(CONFIRMED))
                                .count();
                        return event.getParticipantLimit() != count;
                    }
                    return true;
                })
                .skip(from)
                .limit(size)
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto getEventById(Integer id, HttpServletRequest httpRequest) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (PUBLISHED.equals(event.getState())) {
            sendStatistic(httpRequest);
            getStatistics(List.of(event));
            return EventMapper.toEventFullDto(event);
        }
        throw new NoSuchElementException("Нет опубликованного события с id " + id);
    }

    @Override
    public List<EventShortDto> getUsersEvents(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Event> events = eventRepository.findAllByInitiator(user);
        return events.stream()
                .skip(from)
                .limit(size)
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto createEvent(Integer userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow();
        Event event = EventMapper.toEvent(newEventDto);
        if (LocalDateTime.now().isAfter(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))) {
            throw new BadDataException("Время события не может быть прошедшее! dateTime: " + newEventDto.getEventDate());
        }
        event.setInitiator(user);
        event.setCategory(category);
        Event result = eventRepository.save(event);
        return EventMapper.toEventFullDto(result);
    }

    @Override
    public EventFullDto getFullInformation(Integer userId, Integer eventId) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateUsersEvent(Integer userId, Integer eventId, UpdateEventUserRequest eventRequest) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getState().equals(PUBLISHED)) {
            handleRequestData(eventRequest, event);
        } else {
            log.error("Запрос не соответствует бизнес требованиям : {}, событие :{}", eventRequest, event);
            throw new BadRequestDataException("Запрос не соответствует бизнес требованиям ");
        }
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getInfoAboutRequests(Integer userId, Integer eventId) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Request> requests = event.getRequests();
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Integer userId, Integer eventId,
                                                              EventRequestStatusUpdateRequest request) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        //List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        List<Request> requests = event.getRequests().stream()
                .filter(element -> request.getRequestIds().contains(element.getId()))
                .toList();
        List<Request> requests1 = event.getRequests();
        log.error(" {}", requests1);
        boolean isPending = requests.stream()
                .allMatch(element -> element.getStatus().equals(RequestStatus.PENDING));
        if (!isPending) {
            log.error("В выборке не должно быть запросов кроме статуса PENDING , request : {}", requests);
            throw new BadRequestDataException("В выборке не должно быть запросов кроме статуса PENDING");
        }
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit == 0 || !event.getRequestModeration()) {
            log.error("Данное событие не требует подтверждения");
            throw new BadRequestDataException("Данное событие не требует подтверждения");
        }
        long countConfirmed = event.getRequests().stream()
                .filter(element -> element.getStatus().equals(CONFIRMED))
                .count();
        if (request.getStatus().equals(CONFIRMED)) {
            if ((requests.size() + countConfirmed) <= participantLimit) {
                requests.forEach(element -> element.setStatus(CONFIRMED));

                if ((requests.size() + countConfirmed) == participantLimit) {
                    event.getRequests().stream()
                            .filter(element -> element.getStatus().equals(RequestStatus.PENDING))
                            .forEach(element -> element.setStatus(RequestStatus.REJECTED));
                }
                List<Request> result = event.getRequests();
                return EventRequestStatusUpdateResult.builder()
                        .confirmedRequests(result.stream()
                                .filter(element -> element.getStatus().equals(CONFIRMED))
                                .map(RequestMapper::toParticipationRequestDto)
                                .toList())
                        .rejectedRequests(result.stream()
                                .filter(element -> element.getStatus().equals(RequestStatus.REJECTED))
                                .map(RequestMapper::toParticipationRequestDto)
                                .toList())
                        .build();
            }
            log.error("Превышен лимит подтвержденных запросов");
            throw new BadRequestDataException("Превышен лимит подтвержденных запросов");
        } else if (request.getStatus().equals(RequestStatus.REJECTED)) {
            requests.forEach((element -> element.setStatus(RequestStatus.REJECTED)));
            List<Request> result = event.getRequests();
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(result.stream()
                            .filter(element -> element.getStatus().equals(CONFIRMED))
                            .map(RequestMapper::toParticipationRequestDto)
                            .toList())
                    .rejectedRequests(result.stream()
                            .filter(element -> element.getStatus().equals(RequestStatus.REJECTED))
                            .map(RequestMapper::toParticipationRequestDto)
                            .toList())
                    .build();
        }
        log.error("Ошибка в статусе запроса");
        throw new BadRequestDataException("Ошибка в статусе запроса");
    }

    private Boolean checkDateTime(String dateTime) {
        LocalDateTime time = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        long hours = Duration.between(LocalDateTime.now(), time).toHours();
        return hours >= 2;
    }

    private void handleRequestData(UpdateEventUserRequest request, Event event) {
        if (!isNull(request.getCategory())) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow();
            event.setCategory(category);
        }
        if (!isNull(request.getAnnotation())) {
            event.setAnnotation(request.getAnnotation());
        }
        if (!isNull(request.getDescription())) {
            event.setDescription(request.getDescription());
        }
        if (!isNull(request.getEventDate())) {
            if (checkDateTime(request.getEventDate())) {
                event.setEventDate(request.getEventDate());
            }
            log.error("Неверно указано время события {}", request.getEventDate());
            throw new BadRequestDataException("Неверно указано время события");
        }
        if (!isNull(request.getLocation())) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        if (!isNull(request.getPaid())) {
            event.setPaid(request.getPaid());
        }
        if (!isNull(request.getParticipantLimit())) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (!isNull(request.getRequestModeration())) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (!isNull(request.getTitle())) {
            event.setTitle(request.getTitle());
        }
        if (!isNull(request.getStateAction())) {
            event.setState(request.getStateAction().equals(SEND_TO_REVIEW) ? PENDING : CANCELED);
        }
    }

    private void handleAdminRequestData(UpdateEventAdminRequest request, Event event) {
        if (!isNull(request.getCategory())) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow();
            event.setCategory(category);
        }
        if (!isNull(request.getAnnotation())) {
            event.setAnnotation(request.getAnnotation());
        }
        if (!isNull((request.getTitle()))) {
            event.setTitle(request.getTitle());
        }
        if (!isNull((request.getPaid()))) {
            event.setPaid(request.getPaid());
        }
        if (!isNull(request.getLocation())) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        if (!isNull(request.getRequestModeration())) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (!isNull(request.getEventDate())) {
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(request.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))) {
                throw new BadDataException("Время события не может быть прошедшее! dateTime: " + request.getEventDate());
            }
            event.setEventDate(request.getEventDate());
        }
        if (!isNull(request.getDescription())) {
            event.setDescription(request.getDescription());
        }
        if (!isNull(request.getParticipantLimit())) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (!isNull(request.getStateAction())) {
            if (event.getState().equals(PENDING)) {
                event.setState(request.getStateAction().equals(PUBLISH_EVENT) ? PUBLISHED : CANCELED);
            } else {
                log.error("Событие уже опубликовано: {}", event.getState());
                throw new BadRequestDataException("Событие уже опубликовано");
            }
        }
    }

    private void sendStatistic(HttpServletRequest httpRequest) {
        log.info("Отправляю статистику о посещении события httpRequest: {}", httpRequest);
        baseClient.postHit(EndpointHitDto.builder()
                .app("ewm-service")
                .uri(httpRequest.getRequestURI())
                .ip(httpRequest.getLocalAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }


    public void getStatistics(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());
        List<ViewStatsDto> stats = baseClient.getStats("2000-01-01 00:00:00", "2100-01-01 00:00:00",
                uris, true);
        for (Event event : events) {
            ViewStatsDto currentViewStats = stats.stream()
                    .filter(statsDto -> statsDto.getUri().equals(String.format("/events/%s", event.getId())))
                    .findFirst()
                    .orElse(null);

            Long views = (currentViewStats != null) ? currentViewStats.getHits() : 0;
            event.setViews(views.intValue());
            log.warn("Кол-во просмотров = {}", event.getViews());
        }
    }
}
