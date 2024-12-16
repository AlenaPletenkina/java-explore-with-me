package ru.practicum.ewmservice.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam List<Integer> users,
                                           @RequestParam List<String> states,
                                           @RequestParam List<Integer> categories,
                                           @RequestParam String rangeStart,
                                           @RequestParam String rangeEnd,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получил запрос на получение отфильтрованных событий.");
        return eventService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Получил запрос на обновление события.");
        return eventService.updateEvent(eventId, updateEventAdminRequest);

    }
}
