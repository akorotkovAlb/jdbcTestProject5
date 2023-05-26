package org.example.config;

import org.example.props.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Database {

    private static final H2Database INSTANCE = new H2Database();

    private Connection H2Connection;
    private H2Database () {

        try {
            String postgresConnectionUrl = PropertyReader.getConnectionUrlForH2();
            this.H2Connection = DriverManager.getConnection(postgresConnectionUrl);
        } catch (SQLException e) {
            throw new RuntimeException("Create connection exception");
        }
    }

    public static H2Database getInstance() {
        return INSTANCE;
    }

    public Connection getH2Connection () {
        return H2Connection;
    }

    public int executeUpdate(String query) {
        try(Statement statement = INSTANCE.getH2Connection().createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            H2Connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
