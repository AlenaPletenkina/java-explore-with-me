package ru.practicum.ewmservice.event.mapper;

import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.user.mapper.UserMapper;

import java.util.List;

import static java.util.Objects.isNull;
import static ru.practicum.ewmservice.request.dto.RequestStatus.CONFIRMED;

public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .annotation(event.getAnnotation())
                .views(event.getViews())
                .title(event.getTitle())
                .category(CategoryMapper.categoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(event.getRequests().stream()
                        .filter(request -> request.getStatus().equals(CONFIRMED))
                        .count())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        List<Request> requests = event.getRequests();

        return EventFullDto.builder()
                .id(event.getId())
                .description(event.getDescription())
                .title(event.getTitle())
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .participantLimit(event.getParticipantLimit())
                .eventDate(event.getEventDate())
                .views(event.getViews())
                .category(CategoryMapper.categoryDto(event.getCategory()))
                .requestModeration(event.getRequestModeration())
                .annotation(event.getAnnotation())
                .confirmedRequests(isNull(requests) ? 0 : requests.stream()
                        .filter(request -> CONFIRMED.equals(request.getStatus()))
                        .count())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .title(newEventDto.getTitle())
                .paid(!isNull(newEventDto.getPaid()) && newEventDto.getPaid())
                .state(State.PENDING)
                .participantLimit(isNull(newEventDto.getParticipantLimit()) ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .lon(newEventDto.getLocation().getLon())
                .lat(newEventDto.getLocation().getLat())
                .views(0)
                .build();
    }
}
