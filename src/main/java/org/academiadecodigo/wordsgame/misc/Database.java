package org.academiadecodigo.wordsgame.misc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Database {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public Database() {
        Properties props = new Properties();

        // String env = System.getProperty("env", "dev"); // default to "dev"
        // load it by using java -jar myapp.jar -Denv=prod

        ResourceBundle bundle = ResourceBundle.getBundle("application");
        String env = bundle.getString("env");
        String filename = "application-" + env + ".properties";

        try (InputStream input = Database.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Could not find property file: " + filename);
            }
            props.load(input);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading property file: " + filename, ex);
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
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
}
