package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.user.UserDto;
import ru.practicum.exploreWithMe.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
