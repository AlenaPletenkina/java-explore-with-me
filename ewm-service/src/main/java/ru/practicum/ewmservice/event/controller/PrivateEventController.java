package ru.practicum.ewmservice.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.request.dto.ParticipationRequestDto;

import java.util.List;

@RequestMapping("/users")
@RestController
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final String path1 = "/{user-id}/events";
    private final String path2 = "/{user-id}/events/{event-id}";
    private final String path3 = "/{user-id}/events/{event-id}/requests";

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(path1)
    public List<EventShortDto> getUsersEvents(@PathVariable("user-id") @Min(1) Integer userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getUsersEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path1)
    public EventFullDto createEvent(@PathVariable("user-id") Integer userId,
                                    @RequestBody @Valid NewEventDto request) {
        log.info("Получил запрос на создание события с userId: {}, event: {}", userId, request);
        EventFullDto event = eventService.createEvent(userId, request);
        log.info("Событие создано. event : {}", event);
        return event;
    }

    @GetMapping(path2)
    public EventFullDto getFullInformation(@PathVariable("user-id") Integer userId,
                                           @PathVariable Integer eventId) {
        return eventService.getFullInformation(userId, eventId);
    }

    @PatchMapping(path2)
    public EventFullDto updateEvent(@PathVariable("user-id") Integer userId,
                                    @PathVariable("event-id") Integer eventId,
                                    @RequestBody @Valid UpdateEventUserRequest event) {
        log.info("Получил запрос на обновление события. userId: {}, eventId: {}, event: {}.", userId, eventId, event);
        return eventService.updateUsersEvent(userId, eventId, event);
    }

    @GetMapping(path3)
    public List<ParticipationRequestDto> getInfoAboutRequests(@PathVariable("user-id") Integer userId,
                                                              @PathVariable("event-id") Integer eventId) {
        return eventService.getInfoAboutRequests(userId, eventId);
    }

    @PatchMapping(path3)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable("user-id") Integer userId,
                                                              @PathVariable("event-id") Integer eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        log.info("Получил запрос на обновление статуса заявки userId: {}, eventId: {}, request: {} ", userId, eventId, request);
        return eventService.changeRequestStatus(userId, eventId, request);
    }
}
