package ru.practicum.ewmservice.user.service;

import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Integer> ids, Integer from, Integer size);


    UserDto createUser(NewUserRequest userRequest);

    void deleteUser(Integer userId);
}
