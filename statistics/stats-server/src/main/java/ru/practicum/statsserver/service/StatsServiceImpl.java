package ru.practicum.statsserver.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.mapper.StatsMapper;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void saveEvent(EndpointHitDto event) {
        EndpointHit endpointHit = StatsMapper.toEndpointHit(event);
        EndpointHit result = statsRepository.save(endpointHit);
        log.info("Успешно сохранил событие в базу данных {}", result);
    }

    @Override
    public List<ViewStatsDto> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime parsedStart = LocalDateTime.parse(start, formatter);
        LocalDateTime parsedEnd = LocalDateTime.parse(end, formatter);
        if (uris != null) {
            return convertToViewStatsDto(statsRepository.findAllElements(parsedStart, parsedEnd, unique));
        }
        return convertToViewStatsDto(statsRepository.findAllElements(parsedStart, parsedEnd, uris, unique));
    }


    private List<ViewStatsDto> convertToViewStatsDto(List<Map<String, Object>> result) {
        return result.stream()
                .map(map -> new ViewStatsDto((String) map.get("app"), (String) map.get("uri"), (Long) map.get("hits")))
                .collect(Collectors.toList());
    }

}
