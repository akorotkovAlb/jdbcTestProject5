package org.example;

import org.example.config.MysqlDatabase;
import org.example.config.PostgresDatabase;
import org.example.users.Gender;
import org.example.users.User;
import org.example.users.UserDao;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main (String[] args) {
        Connection connection = MysqlDatabase.getInstance().getMysqlConnection();
        UserDao userDao = new UserDao(connection);

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

        List<User> allActiveUsers = userDao.findAllUserWithActiveStatus(true);
        System.out.println("ACTIVE: ");
        allActiveUsers.forEach(user -> System.out.println(user.toString()));
//
//        List<User> allDeactivateUsers = userDao.findAllUserWithActiveStatus(false);
//        System.out.println("DEACTIVATE: ");
//        allDeactivateUsers.forEach(user -> System.out.println(user.toString()));

//        Optional<User> userUpdateOptional = userDao.updateUser(2L, "Siri1",
//                LocalDate.now(), true, Gender.FEMALE);
//        userUpdateOptional.ifPresent(user -> System.out.println("Result: " + user));

    }
}