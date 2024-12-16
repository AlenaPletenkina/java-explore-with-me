package ru.practicum.ewmservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    private String annotation;
    private Integer category;

    private String description;

    private String eventDate;
    private Location location;
    private Boolean paid;

    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;

    private String title;
}
