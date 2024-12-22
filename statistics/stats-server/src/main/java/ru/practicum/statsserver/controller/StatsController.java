package ru.practicum.statsserver.controller;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserver.service.StatsService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public String saveEvent(@RequestBody EndpointHitDto event) {
        log.info("Получил событие для записи {}", event);
        statsService.saveEvent(event);
        return "Информация сохранена";
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStatistics(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получил запрос на подборку статистики. Параметры : start={}, end={}," +
                " uris={}, unique={}", start, end, uris, unique);
        List<ViewStatsDto> statistics = statsService.getStatistics(start, end, uris, unique);
        log.info("Сформировал статистику response: {}", statistics);
        return statistics;
    }
}
