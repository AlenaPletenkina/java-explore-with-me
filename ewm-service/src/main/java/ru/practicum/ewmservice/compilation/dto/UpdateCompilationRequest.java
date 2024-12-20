package ru.practicum.ewmservice.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private Integer id;
    private Set<Integer> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}
