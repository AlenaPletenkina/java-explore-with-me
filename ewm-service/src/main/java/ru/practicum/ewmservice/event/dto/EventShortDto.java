package ru.practicum.ewmservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {
    private Integer id;
    private String annotation;
    private Long confirmedRequests;
    private String eventDate;
    private Boolean paid;
    private String title;
    private Integer views;
    private CategoryDto category;
    private UserShortDto initiator;
}
