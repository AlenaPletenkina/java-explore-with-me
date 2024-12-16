package ru.practicum.ewmservice.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.event.dto.State;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String annotation;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
    private String createdOn;
    private String description;
    private String eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User initiator;
    private float lon;
    private float lat;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    private Integer views;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Request> requests;
}
