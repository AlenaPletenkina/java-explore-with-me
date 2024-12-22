package ru.practicum.ewmservice.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.mapper.EventMapper;

import static java.util.Objects.isNull;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(!isNull(compilation.getEvents()) ? compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .toList() : null)
                .title(compilation.getTitle())
                .pinned(!isNull(compilation.getPinned()) && compilation.getPinned())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }
}
