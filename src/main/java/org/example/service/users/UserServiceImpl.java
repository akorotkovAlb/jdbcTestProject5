package org.example.service.users;

import org.example.data.daos.UserDao;
import org.example.data.entity.User;
import org.example.service.dto.UserDto;
import org.example.utils.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.service.UserMapper.mapToDtoList;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long create(String name, LocalDate birthday, Gender gender) {
        User newUser = new User(name, birthday, true, gender);
        userDao.saveUser(newUser);
        Optional<User> optionalSavedUser = userDao.findLastUser();
        if (optionalSavedUser.isPresent()) {
            return optionalSavedUser.get().getId();
        } else {
            throw new RuntimeException("User not created!");
        }
    }

    @Override
    public List<UserDto> listAll () {
        return mapToDtoList(userDao.findAllUser());
    }
}
