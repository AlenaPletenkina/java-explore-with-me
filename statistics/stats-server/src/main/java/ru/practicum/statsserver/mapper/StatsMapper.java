package ru.practicum.statsserver.mapper;

import ru.practicum.dto.EndpointHitDto;
import lombok.experimental.UtilityClass;
import ru.practicum.statsserver.model.EndpointHit;
@UtilityClass
public class StatsMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .app(hit.getApp())
                .id(hit.getId())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .uri(hit.getUri())
                .build();
    }
    public static EndpointHit toEndpointHit(EndpointHitDto hit) {
        return EndpointHit.builder()
                .app(hit.getApp())
                .id(hit.getId())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .uri(hit.getUri())
                .build();
    }
}
