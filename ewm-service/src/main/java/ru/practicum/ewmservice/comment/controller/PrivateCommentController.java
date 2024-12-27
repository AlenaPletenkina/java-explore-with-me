package ru.practicum.ewmservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.comment.dto.CommentDto;
import ru.practicum.ewmservice.comment.dto.CommentShortDto;
import ru.practicum.ewmservice.comment.service.CommentService;

@RequestMapping("/users/{userId}/comments")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentShortDto createComment(@PathVariable Integer userId,
                                         @RequestParam Integer eventId,
                                         @RequestBody CommentDto commentDto) {
        log.info("Создание комментария {}", commentDto);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentShortDto updateComment(@PathVariable Integer userId,
                                         @RequestBody CommentDto commentDto,
                                         @PathVariable Integer commentId) {
        log.info("Редактирование комментария {}", commentDto);
        return commentService.updateComment(userId, commentDto, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        log.info("Удаление комментария c id:{}", commentId);
        commentService.deleteComment(userId, commentId);
    }
}
