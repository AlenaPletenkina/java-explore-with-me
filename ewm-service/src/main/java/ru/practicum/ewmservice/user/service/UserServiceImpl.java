package ru.practicum.ewmservice.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exception.UserNotFoundException;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers(List<Integer> ids, Integer from, Integer size) {
        List<User> users = userRepository.findAllById(ids);
       return users.stream()
                .skip(from)
                .limit(size)
                .map(UserMapper::toUserDto)
                .toList();
    }
    @Transactional
    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        User user = UserMapper.toUser(userRequest);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(userId);
    }
}
