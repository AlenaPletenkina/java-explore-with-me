package ru.practicum.ewmservice.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.comment.dto.CommentStatus;
import ru.practicum.ewmservice.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByEventIdAndStatus(Integer eventId, CommentStatus status);
}
