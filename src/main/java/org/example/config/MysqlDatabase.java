package org.example.config;

import org.example.props.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlDatabase {

    private static final MysqlDatabase INSTANCE = new MysqlDatabase();

    private Connection MysqlConnection;
    private MysqlDatabase () {

        try {
            String mysqlConnectionUrl = PropertyReader.getConnectionUrlForMysql();
            this.MysqlConnection = DriverManager.getConnection(mysqlConnectionUrl,
                    PropertyReader.getUserForMysql(),
                    PropertyReader.getPasswordForMysql());
        } catch (SQLException e) {
            throw new RuntimeException("Create connection exception ==> " + e.getMessage());
        }
    }

    public static MysqlDatabase getInstance() {
        return INSTANCE;
    }

    public Connection getMysqlConnection () {
        return MysqlConnection;
    }

    public int executeUpdate(String query) {
        try(Statement statement = INSTANCE.getMysqlConnection().createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            MysqlConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
