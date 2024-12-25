package ru.practicum.ewmservice.request.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.dto.RequestStatus;
import ru.practicum.ewmservice.user.model.User;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "requests")
@Builder
@Table(indexes = {@Index(name = "multi_index",columnList = "requester_id,event_id",unique = true)})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @CreationTimestamp
    private Instant created;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Event event;

}
