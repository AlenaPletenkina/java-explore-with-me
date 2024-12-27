package ru.practicum.ewmservice.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentShortDto {
    @NotBlank
    @Size(min = 5, max = 2000)
    String text;
    UserShortDto author;
    LocalDateTime created;
}
