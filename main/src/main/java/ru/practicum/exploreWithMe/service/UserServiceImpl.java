package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.user.UserDto;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.storage.UserRepository;
import ru.practicum.exploreWithMe.util.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(List<Integer> ids, Integer size, Integer from) {
        int actualFrom = from == 0 ? 0 : from -1;

        List<Integer> actualIds = ids == null ? new ArrayList<>() : ids;

        log.info("Some users with name was viewed.");
        if (!actualIds.isEmpty()) {
            return userRepository.findAllByIdIn(ids);
        } else {
            return userRepository.findAll().stream().skip(actualFrom).limit(size).collect(Collectors.toList());
        }
    }

    @Override
    public User addUser(UserDto userDto) {
        log.info("User with name " + userDto.getName() + " was added.");
        return userRepository.save(UserMapper.toUser(userDto));
    }

    @Override
    public void deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            log.info("User with Id=" + id + " was deleted.");
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("User with Id=" + id + " does not exist.");
        }
    }
}
