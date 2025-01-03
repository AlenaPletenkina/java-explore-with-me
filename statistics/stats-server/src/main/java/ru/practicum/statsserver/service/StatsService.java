package ru.practicum.statsserver.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {
    void saveEvent(EndpointHitDto event);

    List<ViewStatsDto> getStatistics(String start, String end, List<String> uris, Boolean unique);
}
