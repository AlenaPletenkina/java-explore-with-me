package ru.practicum.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseClient {
    final RestTemplate restTemplate = new RestTemplate();
    final String serverUrl = "http://localhost:9090";
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String postHit(EndpointHitDto hit) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(hit);
        return restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, String.class).getBody();
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> urisList, Boolean unique) {
        String uris = String.join(",", urisList);
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique != null ? unique : false
        );
        ViewStatsDto[] result = restTemplate.getForObject(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}", ViewStatsDto[].class, parameters);
        return isNull(result) ? Collections.emptyList() : List.of(result);
    }
}
