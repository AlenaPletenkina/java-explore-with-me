package ru.practicum.ewmservice.compilation.service;

import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllEvents(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Integer compId);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Integer compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Integer compId);
}


