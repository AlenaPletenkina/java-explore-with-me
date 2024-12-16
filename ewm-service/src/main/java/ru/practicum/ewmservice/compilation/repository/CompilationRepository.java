package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation,Integer> {
}
