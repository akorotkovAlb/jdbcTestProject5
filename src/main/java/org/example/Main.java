package org.example;

import org.example.config.PostgresDatabase;
import org.example.data.daos.UserDao;
import org.example.service.dto.UserDto;
import org.example.service.users.UserService;
import org.example.service.users.UserServiceImpl;
import org.example.utils.Gender;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main (String[] args) {
        Connection connection = PostgresDatabase.getInstance().getPostgresConnection();
        UserDao userDao = new UserDao(connection);
        UserService service = new UserServiceImpl(userDao);

        long result = service.create("qwerty", LocalDate.now(), Gender.FEMALE);
        System.out.println("===> " + result);

        List<UserDto> dtos = service.listAll();
        dtos.forEach(dto -> System.out.println("++++> " + dto));


















//        userDao.saveUser("Mark4", LocalDate.now(), true, Gender.MALE);

//        Optional<User> optionalUser2 = userDao.findUserById(2L);
//        if (optionalUser2.isPresent()) {
//            System.out.println("Result: " + optionalUser2.get());
//        }
//
//        Optional<User> optionalUser999 = userDao.findUserById(999L);
//        if (optionalUser999.isPresent()) {
//            System.out.println("Result: " + optionalUser999.get());
//        } else {
//            System.out.println("No user with id = " + 999L);
//        }

//        List<User> allUsers = userDao.findAllUser();
//        allUsers.forEach(user -> System.out.println(user.toString()));

//        List<User> allActiveUsers = userDao.findAllUserWithActiveStatus(true);
//        System.out.println("ACTIVE: ");
//        allActiveUsers.forEach(user -> System.out.println(user.toString()));
//
//        List<User> allDeactivateUsers = userDao.findAllUserWithActiveStatus(false);
//        System.out.println("DEACTIVATE: ");
//        allDeactivateUsers.forEach(user -> System.out.println(user.toString()));

//        Optional<User> userUpdateOptional = userDao.updateUser(2L, "Siri1",
//                LocalDate.now(), true, Gender.FEMALE);
//        userUpdateOptional.ifPresent(user -> System.out.println("Result: " + user));

    }
}