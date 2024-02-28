package org.example.service.users;

import org.example.service.dto.UserDto;
import org.example.utils.Gender;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    long create(String name, LocalDate birthday, Gender gender);

    List<UserDto> listAll();
}
