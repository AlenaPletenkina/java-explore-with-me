package ru.practicum.ewmservice.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.UserNotFoundException;
import ru.practicum.ewmservice.request.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.dto.RequestStatus;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.request.repository.RequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public RequestServiceImpl(UserRepository userRepository, RequestRepository requestRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> getUserRequests(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Request> requests = requestRepository.findByUser(user);
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow();
        Request request = Request.builder()
                .user(user)
                .event(event)
                .status(event.getParticipantLimit() == 0 ? RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        Request result = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(result);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        userRepository.findById(userId).orElseThrow();
        Request request = requestRepository.findById(requestId).orElseThrow();
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }
}
