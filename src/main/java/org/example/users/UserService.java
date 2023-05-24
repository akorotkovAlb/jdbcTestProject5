package org.example.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private static final String INSERT_STRING = "INSERT INTO users VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_STRING = "SELECT id, name, birthday FROM users WHERE id = ?";
    private static final String SELECT_ALL_STRING = "SELECT id, name, birthday FROM users";
    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement selectByStatement;
    private PreparedStatement selectAllStatement;
    private Statement selectAllSimpleStatement;
    private PreparedStatement transactionalPrepareStatement;

    public UserService(Connection connection) {
        this.connection = connection;
        try {
            this.insertStatement = connection.prepareStatement(INSERT_STRING);
            this.selectByStatement = connection.prepareStatement(SELECT_BY_ID_STRING);
            this.selectAllStatement = connection.prepareStatement(SELECT_ALL_STRING);
            this.selectAllSimpleStatement = connection.createStatement();
            this.transactionalPrepareStatement = connection.prepareStatement(INSERT_STRING);
        } catch(SQLException e) {
            System.out.println("User Service construction exception. Reason: " + e.getMessage());
        }
    }

    public int saveUser(Long id, String name, LocalDate birthday) {
        try {
            this.insertStatement.setLong(1, id);
            this.insertStatement.setString(2, name);
            this.insertStatement.setDate(3, java.sql.Date.valueOf(birthday.toString()));
            return this.insertStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Insert user exception. Reason: " + e.getMessage());
            return -1;
        }
    }

    public Optional<User> findUserById(Long id) {
        try {
            this.selectByStatement.setLong(1, id);
            try (ResultSet resultSet = this.selectByStatement.executeQuery()) {
                if(resultSet.next()) {
                    User user = new User(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("birthday")));
                    return Optional.of(user);
                }
            } catch(SQLException e) {
                System.out.println("Select user exception. Reason: " + e.getMessage());
            }
        } catch(SQLException e) {
            System.out.println("Select user exception. Reason: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try(ResultSet resultSet = this.selectAllStatement.executeQuery()) {
            while(resultSet.next()) {
                User user = new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("birthday")));
                users.add(user);
            }
        } catch(SQLException e) {
            System.out.println("Select ALL user exception. Reason: " + e.getMessage());
        }
        return users;
    }

    public List<User> findAllUserStatement() {
        List<User> users = new ArrayList<>();
        try(ResultSet resultSet = this.selectAllSimpleStatement.executeQuery("SELECT id, name, birthday FROM users")) {
            while(resultSet.next()) {
                User user = new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("birthday")));
                users.add(user);
            }
        } catch(SQLException e) {
            System.out.println("Select ALL user exception. Reason: " + e.getMessage());
        }
        return users;
    }

    public void saveAllUsers(List<User> users) {
        try {
            for(User user : users) {
                insertStatement.setLong(1, user.getId());
                insertStatement.setString(2, user.getName());
                insertStatement.setDate(3, java.sql.Date.valueOf(user.getBirthday().toString()));
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch(SQLException e) {
            System.out.println("Insert ALL user exception. Reason: " + e.getMessage());
        }
    }

    public void saveAllUsersWithBatchSize(List<User> users, int batchSize) {
        Map<Long, List<User>> groups =
                users.stream().collect(Collectors.groupingBy(element -> (element.getId() - 1) / batchSize));
        List<List<User>> usersBatches = new ArrayList<>(groups.values());
        try {
            for (List<User> batch : usersBatches) {
                for(User user : batch) {
                    insertStatement.setLong(1, user.getId());
                    insertStatement.setString(2, user.getName());
                    insertStatement.setDate(3, java.sql.Date.valueOf(user.getBirthday().toString()));
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }
        } catch(SQLException e) {
            System.out.println("Insert ALL user batches exception. Reason: " + e.getMessage());
        }
    }

    public List<User> generateTestUsers(long startId, long countUsersForButches, String name, int minusYears) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < countUsersForButches; i++) {
            long id = startId + i;
            users.add(new User(id, name + id, LocalDate.now().minusYears(minusYears)));
        }
        return users;
    }

    public void transactionalInsert(List<User> users) throws SQLException {
        try {
            this.connection.setAutoCommit(false);
            for(User user : users) {
                transactionalPrepareStatement.setLong(1, user.getId());
                transactionalPrepareStatement.setString(2, user.getName());
                transactionalPrepareStatement.setDate(3, java.sql.Date.valueOf(user.getBirthday().toString()));
                transactionalPrepareStatement.addBatch();
            }
            try {
                transactionalPrepareStatement.executeBatch();
                connection.commit();
            } catch(SQLException e) {
                System.out.println("TRANSACTIONAL FAIL. Rollback changes. Reason: " + e.getMessage());
                connection.rollback();
                throw e;
            }
        } catch(SQLException e) {
            System.out.println("TRANSACTIONAL method SQL exception. Reason: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
