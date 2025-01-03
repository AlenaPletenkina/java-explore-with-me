package ru.practicum.ewmservice.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.exception.CompilationNotFoundException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CompilationDto> getAllEvents(Boolean pinned, Integer from, Integer size) {
        List<Compilation> all = compilationRepository.findAll();
        List<CompilationDto> result = all.stream()
                .filter(compilation -> {
                    if (isNull(pinned)) {
                        return true;
                    } else if (isNull(compilation.getPinned())) {
                        return false;
                    }
                    return compilation.getPinned().equals(pinned);
                })
                .skip(from)
                .limit(size)
                .map(CompilationMapper::toCompilationDto)
                .toList();
        log.info("Нашел все компиляции размером {}", all.size());
        log.info("Отфильтровал компиляцию {}", result.size());
        return result;
    }

    @Override
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        if (!isNull(compilationDto.getEvents())) {
            List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
            compilation.setEvents(events);
        }
        Compilation result = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(result);
    }

    @Transactional
    @Override
    public void deleteCompilation(Integer compId) {
        boolean exists = compilationRepository.existsById(compId);
        if (!exists) {
            throw new CompilationNotFoundException();
        }
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        if (!isNull(updateCompilationRequest.getEvents())) {
            compilation.setEvents(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        }
        compilation.setPinned(updateCompilationRequest.getPinned());
        compilation.setTitle(updateCompilationRequest.getTitle());
        return CompilationMapper.toCompilationDto(compilation);
    }
}
