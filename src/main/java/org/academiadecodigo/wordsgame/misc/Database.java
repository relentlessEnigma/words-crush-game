package org.academiadecodigo.wordsgame.misc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Database {
    private Connection connection;
    private String env;
    private String completeUrl;
    private String url;
    private String username;
    private String password;
    private Properties props;
    private String dbName;

    public Database() throws SQLException {
        this.props = new Properties();

        // String env = System.getProperty("env", "dev"); // default to "dev"
        // load it by using java -jar myapp.jar -Denv=prod

        ResourceBundle bundle = ResourceBundle.getBundle("application");
        env = bundle.getString("env");
        String filename = "application-" + env + ".properties";

        try (InputStream input = Database.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Could not find property file: " + filename);
            }
            props.load(input);
            completeUrl = props.getProperty("db.completeUrl");
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            dbName = props.getProperty("db.name");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading property file: " + filename, ex);
        }
    }

    public void setupDbTable() throws SQLException {
        connect();
        if(env.equals("dev")) {
            //create db if not exists and insert some mockup data.
            String query = "CREATE DATABASE IF NOT EXISTS " + dbName;
            executeUpdate(query);
            query = "USE " + dbName;
            executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER', " +
                    "PRIMARY KEY (id)" +
                    ")";
            executeUpdate(query);
            query = "INSERT INTO users (username, password, role) VALUES ('admin', 'pass', 'ADMIN');";
            executeUpdate(query);
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(completeUrl, username, password);
            } catch (SQLSyntaxErrorException e) {
                connection = DriverManager.getConnection(url, username, password);
            }
        }
        return connection;
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
