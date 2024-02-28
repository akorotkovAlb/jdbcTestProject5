package org.example.service;

import org.example.data.entity.User;
import org.example.service.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static List<UserDto> mapToDtoList(List<User> entities) {
        return entities.stream()
                .map(entity -> mapToDto(entity))
                .collect(Collectors.toList());
    }

    private static UserDto mapToDto (User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBirthday(entity.getBirthday());
        dto.setActive(entity.isActive());
        dto.setGender(entity.getGender());
        return dto;
    }
}
