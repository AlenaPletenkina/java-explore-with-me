package ru.practicum.ewmservice.request.mapper;

import ru.practicum.ewmservice.request.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.request.model.Request;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getUser().getId())
                .created(request.getCreated().toString())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .build();
    }
}
