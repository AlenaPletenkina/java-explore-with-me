package ru.practicum.ewmservice.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j

public class PublicCompilationController {
    private final CompilationService compilationService;

    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAllEvents(@RequestParam(required = false) Boolean pinned,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получил запрос на получение списка всех событий.");
        return compilationService.getAllEvents(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("Получил запрос на получение списка событий по его id.");
        return compilationService.getCompilationById(compId);
    }


}
