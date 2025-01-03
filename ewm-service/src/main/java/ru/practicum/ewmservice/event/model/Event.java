package ru.practicum.ewmservice.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.event.dto.State;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Data
@ToString(exclude = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    private String createdOn;
    @Column(name = "description", length = 7000)
    private String description;
    private String eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User initiator;
    private float lon;
    private float lat;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    private String publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;
    private String title;
    private Integer views;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
    private List<Request> requests;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
    private List<Comment> comments;
}
