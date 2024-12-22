package ru.practicum.ewmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BadDataException extends RuntimeException {
    public BadDataException(String message) {
    }
}
