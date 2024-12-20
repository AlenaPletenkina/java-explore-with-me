package ru.practicum.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiator(User user);

    @Query("SELECT event FROM Event event " +
            "WHERE (:users IS NULL OR event.initiator.id IN :users) " +
            "AND (:states IS NULL OR event.state IN :states) " +
            "AND (:categories IS NULL OR event.category.id IN :categories) " +
            "AND (:rangeStart IS NULL OR event.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR event.eventDate <= :rangeEnd)"
    )
    List<Event> getEventsWithFilter(List<Integer> users, List<String> states, List<Integer> categories,
                                    String rangeStart, String rangeEnd);

    @Query("SELECT event FROM Event event " +
            "WHERE event.state='PUBLISHED'" +
            "AND (:text IS NULL OR (LOWER(event.description) LIKE %:text% OR LOWER(event.annotation) LIKE %:text%)) " +
            "AND (:categories IS NULL OR event.category.id IN :categories) " +
            "AND (:paid IS NULL OR event.paid IN :paid) " +
            "AND (:rangeStart IS NULL OR event.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR event.eventDate <= :rangeEnd) " +
            "ORDER BY  event.eventDate ")
    List<Event> getPublicEventsWithFilter(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                          String rangeEnd);


}
