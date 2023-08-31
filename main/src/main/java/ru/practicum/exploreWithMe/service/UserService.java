package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.user.UserDto;
import ru.practicum.exploreWithMe.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(List<Integer> ids, Integer size, Integer from);

    User addUser(UserDto userDto);

    void deleteUser(Integer id);
}
