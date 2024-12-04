package ru.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT new map(hit.app as app,hit.uri as uri, (CASE :unique " +
            "        WHEN true THEN COUNT(DISTINCT hit.ip) " +
            "        ELSE COUNT(hit.ip) END) as hits)"+
            "FROM EndpointHit hit "+
            "WHERE hit.timestamp BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")

    List<Map<String,Object>> findAllElements(LocalDateTime start, LocalDateTime end, Boolean unique);

    @Query("SELECT new map(hit.app as app,hit.uri as uri, (CASE :unique " +
            "        WHEN true THEN COUNT(DISTINCT hit.ip) " +
            "        ELSE COUNT(hit.ip) END) as hits)"+
            "FROM EndpointHit AS hit " +
            "WHERE hit.uri IN :uris AND hit.timestamp BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<Map<String, Object>> findAllElements(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
