package ru.practicum.ewmservice.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.dto.Sort;
import ru.practicum.ewmservice.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getPublicEvents(HttpServletRequest httpRequest,
                                               @RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) Sort sort,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получил запрос на получение публичных событий. Categories: {}", categories);
        return eventService.getPublicEvents(httpRequest, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Integer id, HttpServletRequest httpRequest) {
        log.info("Получил запрос на получение события по его id : {}", id);
        EventFullDto eventById = eventService.getEventById(id, httpRequest);
        log.info("Получил событие : {}", eventById);
        return eventById;
    }

}
