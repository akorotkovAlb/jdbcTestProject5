package org.example;

import org.example.config.MysqlDatabase;
import org.example.config.PostgresDatabase;
import org.example.users.User;
import org.example.users.UserService;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main (String[] args) {
        Connection connection = PostgresDatabase.getInstance().getPostgresConnection();
        UserService service = new UserService(connection);

        //TODO: use insert method
//        int result = service.saveUser(7L, "Mark", LocalDate.now()
//                .minusYears(18).minusMonths(2));
//        System.out.println("==>> " + result);















        //TODO: use select by id method
//        Optional<User> optionalUser = service.findUserById(44L);
//        if (optionalUser.isPresent()) {
//            System.out.println("User -> " + optionalUser.get().toString());
//        } else {
//            System.out.println("No user in db with given id.");
//        }


















        //TODO: use select without parameters (select all users)
//        List<User> allUsers = service.findAllUser();
//        System.out.println("Users in list -> " + allUsers.size());
//        allUsers.forEach(user -> System.out.println("User -->> " + user.toString()));



















        //TODO: compare Statement and prepareStatement
//        long startPrepareStatement = System.currentTimeMillis();
//        for(int i = 0; i < 1000; i++) {
//            service.findAllUser();
//        }
//        long finishPrepareStatement = System.currentTimeMillis();
//        long prepareStatementResult = finishPrepareStatement - startPrepareStatement;
//        System.out.println("Prepare statement ===> " + prepareStatementResult);
//
//        long startSimpleStatement = System.currentTimeMillis();
//        for(int i = 0; i < 1000; i++) {
//            service.findAllUserStatement();
//        }
//        long finishSimpleStatement = System.currentTimeMillis();
//        long simpleStatementResult = finishSimpleStatement - startSimpleStatement;
//        System.out.println("Simple statement ===> " + simpleStatementResult);
//
//        long result = simpleStatementResult - prepareStatementResult;
//        System.out.println("Diff ===> " + result);

























        //TODO generate random user and add to db
        // 100 user insert vs 100 user as batch update
//        long startId = 8;
//        long startSimpleInsert = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            long id = startId + i;
//            service.saveUser(id, "Mark " + id, LocalDate.now().minusYears(20));
//        }
//        long finishSimpleInsert = System.currentTimeMillis();
//        long resultSimpleInsert = finishSimpleInsert - startSimpleInsert;
//        System.out.println("Start single insert ===> " + startSimpleInsert);
//        System.out.println("Finish single insert ===> " + finishSimpleInsert);
//        System.out.println("Result single insert ===> " + resultSimpleInsert);
//
//        List<User> users = new ArrayList<>();
//        long startBatchId = startId + 100;
//        long startBatchInsert = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            long id = startBatchId + i;
//            users.add(new User(id, "Ihor " + id, LocalDate.now().minusYears(25)));
//        }
//        service.saveAllUsers(users);
//        long finishBatchInsert = System.currentTimeMillis();
//        long resultBatchInsert = finishBatchInsert - startBatchInsert;
//        System.out.println("Start Batch insert ===> " + startBatchInsert);
//        System.out.println("Finish Batch insert ===> " + finishBatchInsert);
//        System.out.println("Result Batch insert ===> " + resultBatchInsert);
//        long result = resultSimpleInsert - resultBatchInsert;
//        System.out.println("RESULT --------->>> " + result);































        //TODO generate random 1 000 000 users and add to db
        // 10, 100, 1000, 10 000, 100 000 and 1 000 000 users batches and check batch speed
//        long startId = 8;
//        long countUsersForButches = 1000000;
//
//        List<User> users10Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Alisa 10 batches  ", 25);
//        System.out.println("Created 1000000 for 10 batch");
//        startId += countUsersForButches;
//        List<User> users100Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Milla 100 batches  ", 15);
//        System.out.println("Created 1000000 for 100 batch");
//        startId += countUsersForButches;
//        List<User> users1000Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Lyda 1000 batches  ", 20);
//        System.out.println("Created 1000000 for 1000 batch");
//        startId += countUsersForButches;
//        List<User> users10000Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Inna 10000 batches  ", 30);
//        System.out.println("Created 1000000 for 10000 batch");
//        startId += countUsersForButches;
//        List<User> users100000Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Olga 100000 batches  ", 40);
//        System.out.println("Created 1000000 for 100000 batch");
//        startId += countUsersForButches;
//        List<User> users1000000Batch = service.generateTestUsers(startId, countUsersForButches,
//                "Anna 1000000 batches  ", 50);
//        System.out.println("Created 1000000 for 1000000 batch");
//
//        System.out.println("Start insert 1000000 by 10 in batch");
//        long start10 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users10Batch, 10);
//        long finish10 = System.currentTimeMillis();
//        long result10 = finish10 - start10;
//        System.out.println("10 batch result ==>> " + result10);
//
//        System.out.println("Start insert 1000000 by 100 in batch");
//        long start100 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users100Batch, 100);
//        long finish100 = System.currentTimeMillis();
//        long result100 = finish100 - start100;
//        System.out.println("100 batch result ==>> " + result100);
//
//        System.out.println("Start insert 1000000 by 1000 in batch");
//        long start1000 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users1000Batch, 1000);
//        long finish1000 = System.currentTimeMillis();
//        long result1000 = finish1000 - start1000;
//        System.out.println("1000 batch result ==>> " + result1000);
//
//        System.out.println("Start insert 1000000 by 10000 in batch");
//        long start10000 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users10000Batch, 10000);
//        long finish10000 = System.currentTimeMillis();
//        long result10000 = finish10000 - start10000;
//        System.out.println("10000 batch result ==>> " + result10000);
//
//        System.out.println("Start insert 1000000 by 100000 in batch");
//        long start100000 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users100000Batch, 100000);
//        long finish100000 = System.currentTimeMillis();
//        long result100000 = finish100000 - start100000;
//        System.out.println("100000 batch result ==>> " + result100000);
//
//        System.out.println("Start insert 1000000 by 1000000 in batch");
//        long start1000000 = System.currentTimeMillis();
//        service.saveAllUsersWithBatchSize(users1000000Batch, 1000000);
//        long finish1000000 = System.currentTimeMillis();
//        long result1000000 = finish1000000 - start1000000;
//        System.out.println("1000000 batch result ==>> " + result1000000);


























        //TODO: transaction insert 10 users (fail transactional insert)
//        long startId = 7; // in DB user with this id already exist
//        long countUsersForButches = 10;
//        List<User> users = service.generateTestUsers(startId, countUsersForButches,
//                "Transactional Alisa 10 ", 25);
//        try {
//            service.transactionalInsert(users);
//        } catch(SQLException e) {
//            System.out.println("FAIL change autoCommit to active optional.");
//        }





















        //TODO: transaction insert 10 users (success transactional insert)
//        long startId = 8;
//        long countUsersForButches = 10;
//        List<User> users = service.generateTestUsers(startId, countUsersForButches,
//                "Transactional Alisa 10 ", 25);
//        try {
//            service.transactionalInsert(users);
//        } catch(SQLException e) {
//            System.out.println("FAIL change autoCommit to active optional.");
//        }
    }
}