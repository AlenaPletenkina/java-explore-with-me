package ru.practicum.ewmservice.comment.mapper;

import ru.practicum.ewmservice.comment.dto.CommentDto;
import ru.practicum.ewmservice.comment.dto.CommentShortDto;
import ru.practicum.ewmservice.comment.model.Comment;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.ewmservice.comment.dto.CommentStatus.SEND_TO_REVIEW;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(SEND_TO_REVIEW)
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
