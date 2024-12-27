package ru.practicum.ewmservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.comment.dto.CommentShortDto;
import ru.practicum.ewmservice.comment.service.CommentService;

import java.util.List;

@RequestMapping(path = "/comments")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    private List<CommentShortDto> getAllCommentsByEventId(@PathVariable Integer eventId) {
        return commentService.getAllCommentsByEventId(eventId);
    }
}
