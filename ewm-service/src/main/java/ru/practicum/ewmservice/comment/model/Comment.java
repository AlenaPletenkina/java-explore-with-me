package ru.practicum.ewmservice.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.comment.dto.CommentStatus;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotBlank
    String text;
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Event event;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    CommentStatus status;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
}
