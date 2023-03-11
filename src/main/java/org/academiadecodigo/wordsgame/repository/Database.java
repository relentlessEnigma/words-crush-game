package org.academiadecodigo.wordsgame.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Database {

    private Properties props;
    private Connection connection;
    private String env;
    private DatabaseEnvData dataBaseData;

    public Database() throws SQLException {
        this.props = new Properties();
        env = ResourceBundle.getBundle("application").getString("env");
        dataBaseData = setVarsFromCurrentEnvFile("application-" + env + ".properties");
        this.connection = connect();
    }

    public DatabaseEnvData setVarsFromCurrentEnvFile(String envFile) {

        try (InputStream input = Database.class.getClassLoader().getResourceAsStream(envFile)) {
            if (input == null) {
                throw new RuntimeException("Could not find property file: " + envFile);
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading property file: " + envFile, ex);
        }

        return new DatabaseEnvData(
                props.getProperty("db.completeUrl"),
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password"),
                props.getProperty("db.name"),
                props.getProperty("db.gameAdmin"),
                props.getProperty("db.gameAdminPass")
        );
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager
                        .getConnection(dataBaseData.completeUrl, dataBaseData.dbRoot, dataBaseData.dbRootPass);
            } catch (SQLException e) {
                connection = DriverManager
                        .getConnection(dataBaseData.url, dataBaseData.dbRoot, dataBaseData.dbRootPass);
                setupDbTable();
            }
        }
        return connection;
    }

    public void setupDbTable() throws SQLException {

        String query = "CREATE DATABASE IF NOT EXISTS " + dataBaseData.getDbName();
        executeUpdate(query);

        query = "USE " + dataBaseData.getDbName();
        executeUpdate(query);

        query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(50) NOT NULL, " +
                "role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER', " +
                "PRIMARY KEY (id)" +
                ")";
        executeUpdate(query);
        query = String.format("INSERT INTO users (username, password, role) VALUES ('%s', '%s', 'ADMIN');",
                dataBaseData.gameRoot, dataBaseData.gameRootPass);
        executeUpdate(query);
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute query: " + query);
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Unable to execute update: " + query);
            e.printStackTrace();
            return -1;
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to close database connection.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
