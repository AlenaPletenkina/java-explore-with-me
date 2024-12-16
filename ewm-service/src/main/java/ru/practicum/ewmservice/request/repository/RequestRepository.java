package ru.practicum.ewmservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Integer> {
    List<Request> findByUser(User user);

}
