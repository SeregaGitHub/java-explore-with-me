package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.user.UserDto;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers(@RequestParam(value = "ids", required = false) List<Integer> ids,
                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                               @RequestParam(value = "from", defaultValue = "0") Integer from) {
        return userService.getUsers(ids, size, from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }
}
