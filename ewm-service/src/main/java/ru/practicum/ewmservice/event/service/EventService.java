package ru.practicum.ewmservice.event.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.request.dto.ParticipationRequestDto;

import java.net.http.HttpRequest;
import java.util.List;

public interface EventService {

    List<EventFullDto> getAllEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart,
                                    String rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getPublicEvents(HttpServletRequest httpRequest,String text, List<Integer> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size);

    EventFullDto getEventById(Integer id, HttpServletRequest httpRequest);

    List<EventShortDto> getUsersEvents(Integer userId, Integer from, Integer size);

    EventFullDto createEvent(Integer userId, NewEventDto event);

    EventFullDto getFullInformation(Integer userId, Integer eventId);

    EventFullDto updateUsersEvent(Integer userId, Integer eventId, UpdateEventUserRequest event);

    List<ParticipationRequestDto> getInfoAboutRequests(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult changeRequestStatus(Integer userId, Integer eventId,
                                                       EventRequestStatusUpdateRequest request);


}
