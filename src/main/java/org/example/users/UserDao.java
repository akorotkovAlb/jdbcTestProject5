package org.example.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final String INSERT_STRING = "INSERT INTO users (name, birthday, active, gender) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_STRING = "SELECT id, name, birthday, active, gender FROM users WHERE id = ?";
    private static final String SELECT_ALL_STRING = "SELECT * FROM users";
    private static final String SELECT_ALL_ACTIVE_STRING = "SELECT * FROM users WHERE active = ?";
    private static final String UPDATE_USER_STRING = "UPDATE users SET name = ?, birthday = ?, " +
            "active = ?, gender = ? WHERE id = ?";

    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement selectByStatement;
    private PreparedStatement selectAllStatement;
    private PreparedStatement selectAllActiveStatement;
    private PreparedStatement updateStatement;

    public UserDao(Connection connection) {
        this.connection = connection;
        try {
            this.insertStatement = connection.prepareStatement(INSERT_STRING);
            this.selectByStatement = connection.prepareStatement(SELECT_BY_ID_STRING);
            this.selectAllStatement = connection.prepareStatement(SELECT_ALL_STRING);
            this.selectAllActiveStatement = connection.prepareStatement(SELECT_ALL_ACTIVE_STRING);
            this.updateStatement = connection.prepareStatement(UPDATE_USER_STRING);
        } catch(SQLException e) {
            System.out.println("User Service construction exception. Reason: " + e.getMessage());
        }
    }

    public int saveUser(String name, LocalDate birthday, boolean active, Gender gender) {
        try {
            this.insertStatement.setString(1, name);
            this.insertStatement.setDate(2, java.sql.Date.valueOf(birthday.toString()));
            this.insertStatement.setBoolean(3, active);
            this.insertStatement.setString(4, gender.name());
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
                            LocalDate.parse(resultSet.getString("birthday")),
                            resultSet.getBoolean("active"),
                            Gender.valueOf(resultSet.getString("gender")));
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
                        LocalDate.parse(resultSet.getString("birthday")),
                        resultSet.getBoolean("active"),
                        Gender.valueOf(resultSet.getString("gender")));
                users.add(user);
            }
        } catch(SQLException e) {
            System.out.println("Select ALL user exception. Reason: " + e.getMessage());
        }
        return users;
    }

    public List<User> findAllUserWithActiveStatus(boolean activeStatus) {
        List<User> users = new ArrayList<>();
        try {
            this.selectAllActiveStatement.setBoolean(1, activeStatus);
            try(ResultSet resultSet = this.selectAllActiveStatement.executeQuery()) {
                while(resultSet.next()) {
                    User user = new User(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("birthday")),
                            resultSet.getBoolean("active"),
                            Gender.valueOf(resultSet.getString("gender")));
                    users.add(user);
                }
            } catch(SQLException e) {
                System.out.println("Select ALL ACTIVE user exception. Reason: " + e.getMessage());
            }
        } catch(SQLException e) {
            System.out.println("Select ALL ACTIVE user exception. Reason: " + e.getMessage());
        }
        return users;
    }

    public Optional<User> updateUser(Long id, String name, LocalDate birthday,
                                     boolean active, Gender gender) {
        try {
            this.updateStatement.setString(1, name);
            this.updateStatement.setDate(2, java.sql.Date.valueOf(birthday.toString()));
            this.updateStatement.setBoolean(3, active);
            this.updateStatement.setString(4, gender.name());
            this.updateStatement.setLong(5, id);

            this.updateStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Update user exception. Reason: " + e.getMessage());
        }
        return findUserById(id);
    }
}
