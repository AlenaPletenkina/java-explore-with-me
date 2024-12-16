package ru.practicum.ewmservice.event.controller;

import lombok.extern.slf4j.Slf4j;
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
    public List<EventShortDto> getUsersEvents(@PathVariable Integer userId,
                                              @RequestParam Integer from,
                                              @RequestParam Integer size) {
        return eventService.getUsersEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Integer userId,
                                    @RequestBody NewEventDto event) {
        return eventService.createEvent(userId, event);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullInformation(@PathVariable Integer userId,
                                           @PathVariable Integer eventId) {
        return eventService.getFullInformation(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @RequestBody UpdateEventUserRequest event) {
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
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.changeRequestStatus(userId,eventId,request);
    }
}
