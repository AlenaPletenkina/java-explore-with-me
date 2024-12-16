package ru.practicum.ewmservice.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam List<Integer> ids,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size){
        return  userService.getAllUsers(ids,from,size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser (@PathVariable Integer userId){
        userService.deleteUser(userId);
    }
}
