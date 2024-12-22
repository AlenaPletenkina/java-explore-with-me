package ru.practicum.ewmservice.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUsersEvents(@PathVariable @Min(1) Integer userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getUsersEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Integer userId,
                                    @RequestBody @Valid NewEventDto request) {
        log.info("Получил запрос на создание события с userId: {}, event: {}", userId, request);
        EventFullDto event = eventService.createEvent(userId, request);
        log.info("Событие создано. event : {}", event);
        return event;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullInformation(@PathVariable Integer userId,
                                           @PathVariable Integer eventId) {
        return eventService.getFullInformation(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventUserRequest event) {
        log.info("Получил запрос на обновление события. userId: {}, eventId: {}, event: {}.", userId, eventId, event);
        return eventService.updateUsersEvent(userId, eventId, event);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getInfoAboutRequests(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId) {
        return eventService.getInfoAboutRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        log.info("Получил запрос на обновление статуса заявки userId: {}, eventId: {}, request: {} ", userId, eventId, request);
        return eventService.changeRequestStatus(userId, eventId, request);
    }
}
