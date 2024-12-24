package ru.practicum.ewmservice.compilation.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final String path = "/{comp-id}";

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("Получил запрос на создание подборки, request: {}", compilationDto);
        CompilationDto compilation = compilationService.createCompilation(compilationDto);
        log.info("Получил ответ на создание подборки, response: {}", compilation);
        return compilation;
    }

    @DeleteMapping(path)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("comp-id") Integer compId) {
        log.info("Получил запрос на удаление подборки. {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(path)
    public CompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable("comp-id") Integer compId) {
        log.info("Получил запрос на обновление подборки request: {}, compId: {}.", updateCompilationRequest, compId);
        CompilationDto compilationDto = compilationService.updateCompilation(updateCompilationRequest, compId);
        log.info("Получил ответ на обновление подборки, response: {}", compilationDto);
        return compilationDto;
    }


}
