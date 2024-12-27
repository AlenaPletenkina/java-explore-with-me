package ru.practicum.ewmservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.comment.dto.CommentShortDto;
import ru.practicum.ewmservice.comment.dto.CommentStatus;
import ru.practicum.ewmservice.comment.service.CommentService;

@RequestMapping("/admin/comments")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        log.info("Удаление комментария c id:{}", commentId);
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentShortDto changeCommentStatus(@PathVariable Integer commentId,
                                               @RequestParam CommentStatus status) {
        return commentService.changeCommentStatus(commentId, status);
    }
}
