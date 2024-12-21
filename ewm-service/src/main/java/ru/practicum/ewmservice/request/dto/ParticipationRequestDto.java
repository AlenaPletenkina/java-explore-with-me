package ru.practicum.ewmservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    private String created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private RequestStatus status;

}
