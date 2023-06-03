package org.example.mod5;

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

public class OldUserService {

    private static final String INSERT_STRING = "INSERT INTO OldUsers VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_STRING = "SELECT id, name, birthday FROM OldUsers WHERE id = ?";
    private static final String SELECT_ALL_STRING = "SELECT id, name, birthday FROM OldUsers";
    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement selectByStatement;
    private PreparedStatement selectAllStatement;
    private Statement selectAllSimpleStatement;
    private PreparedStatement transactionalPrepareStatement;
    private PreparedStatement transactionalPrepareStatement2;

    public OldUserService (Connection connection) {
        this.connection = connection;
        try {
            this.insertStatement = connection.prepareStatement(INSERT_STRING);
            this.selectByStatement = connection.prepareStatement(SELECT_BY_ID_STRING);
            this.selectAllStatement = connection.prepareStatement(SELECT_ALL_STRING);
            this.selectAllSimpleStatement = connection.createStatement();
            this.transactionalPrepareStatement = connection.prepareStatement(INSERT_STRING);
            this.transactionalPrepareStatement2 = connection.prepareStatement(INSERT_STRING);
        } catch(SQLException e) {
            System.out.println("OldUser Service construction exception. Reason: " + e.getMessage());
        }
    }

    public int saveOldUser(Long id, String name, LocalDate birthday) {
        try {
            this.insertStatement.setLong(1, id);
            this.insertStatement.setString(2, name);
            this.insertStatement.setDate(3, java.sql.Date.valueOf(birthday.toString()));
            return this.insertStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Insert OldUser exception. Reason: " + e.getMessage());
            return -1;
        }
    }

    public Optional<OldUser> findOldUserById(Long id) {
        try {
            this.selectByStatement.setLong(1, id);
            try (ResultSet resultSet = this.selectByStatement.executeQuery()) {
                if(resultSet.next()) {
                    OldUser OldUser = new OldUser(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("birthday")));
                    return Optional.of(OldUser);
                }
            } catch(SQLException e) {
                System.out.println("Select OldUser exception. Reason: " + e.getMessage());
            }
        } catch(SQLException e) {
            System.out.println("Select OldUser exception. Reason: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<OldUser> findAllOldUser() {
        List<OldUser> OldUsers = new ArrayList<>();
        try(ResultSet resultSet = this.selectAllStatement.executeQuery()) {
            while(resultSet.next()) {
                OldUser OldUser = new OldUser(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("birthday")));
                OldUsers.add(OldUser);
            }
        } catch(SQLException e) {
            System.out.println("Select ALL OldUser exception. Reason: " + e.getMessage());
        }
        return OldUsers;
    }

    public List<OldUser> findAllOldUserStatement() {
        List<OldUser> OldUsers = new ArrayList<>();
        try(ResultSet resultSet = this.selectAllSimpleStatement.executeQuery("SELECT id, name, birthday FROM OldUsers")) {
            while(resultSet.next()) {
                OldUser OldUser = new OldUser(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("birthday")));
                OldUsers.add(OldUser);
            }
        } catch(SQLException e) {
            System.out.println("Select ALL OldUser exception. Reason: " + e.getMessage());
        }
        return OldUsers;
    }

    public void saveAllOldUsers(List<OldUser> OldUsers) {
        try {
            for(OldUser OldUser : OldUsers) {
                insertStatement.setLong(1, OldUser.getId());
                insertStatement.setString(2, OldUser.getName());
                insertStatement.setDate(3, java.sql.Date.valueOf(OldUser.getBirthday().toString()));
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch(SQLException e) {
            System.out.println("Insert ALL OldUser exception. Reason: " + e.getMessage());
        }
    }

    public void saveAllOldUsersWithBatchSize(List<OldUser> OldUsers, int batchSize) {
        Map<Long, List<OldUser>> groups =
                OldUsers.stream().collect(Collectors.groupingBy(element -> (element.getId() - 1) / batchSize));
        List<List<OldUser>> OldUsersBatches = new ArrayList<>(groups.values());
        try {
            for (List<OldUser> batch : OldUsersBatches) {
                for(OldUser OldUser : batch) {
                    insertStatement.setLong(1, OldUser.getId());
                    insertStatement.setString(2, OldUser.getName());
                    insertStatement.setDate(3, java.sql.Date.valueOf(OldUser.getBirthday().toString()));
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }
        } catch(SQLException e) {
            System.out.println("Insert ALL OldUser batches exception. Reason: " + e.getMessage());
        }
    }

    public List<OldUser> generateTestOldUsers(long startId, long countOldUsersForButches, String name, int minusYears) {
        List<OldUser> OldUsers = new ArrayList<>();
        for (int i = 0; i < countOldUsersForButches; i++) {
            long id = startId + i;
            OldUsers.add(new OldUser(id, name + id, LocalDate.now().minusYears(minusYears)));
        }
        return OldUsers;
    }

    public void transactionalInsert(List<OldUser> OldUsers1, List<OldUser> OldUsers2) throws SQLException {
        try {
            this.connection.setAutoCommit(false);
            for(OldUser OldUser : OldUsers1) {
                transactionalPrepareStatement.setLong(1, OldUser.getId());
                transactionalPrepareStatement.setString(2, OldUser.getName());
                transactionalPrepareStatement.setDate(3, java.sql.Date.valueOf(OldUser.getBirthday().toString()));
                transactionalPrepareStatement.addBatch();
            }
            for(OldUser OldUser : OldUsers2) {
                transactionalPrepareStatement2.setLong(1, OldUser.getId());
                transactionalPrepareStatement2.setString(2, OldUser.getName());
                transactionalPrepareStatement2.setDate(3, java.sql.Date.valueOf(OldUser.getBirthday().toString()));
                transactionalPrepareStatement2.addBatch();
            }
            try {
                transactionalPrepareStatement.executeBatch();
                transactionalPrepareStatement2.executeBatch();
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
