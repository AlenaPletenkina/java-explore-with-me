package ru.practicum.ewmservice.comment.service;

import ru.practicum.ewmservice.comment.dto.CommentDto;
import ru.practicum.ewmservice.comment.dto.CommentShortDto;
import ru.practicum.ewmservice.comment.dto.CommentStatus;

import java.util.List;

public interface CommentService {
    CommentShortDto createComment(Integer userId, Integer eventId, CommentDto commentDto);

    CommentShortDto updateComment(Integer userId, CommentDto commentDto, Integer commentId);

    void deleteComment(Integer userId, Integer commentId);

    void deleteComment(Integer commentId);

    List<CommentShortDto> getAllCommentsByEventId(Integer eventId);

    CommentShortDto changeCommentStatus(Integer commentId, CommentStatus status);

}
