package org.academiadecodigo.wordsgame.database;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.entities.users.Roles;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Getter
@Setter
public class Database {

    private static volatile Database instance;
    private Properties props;
    private Connection connection;
    private String env;
    private DatabaseEnvData dataBaseData;

    private Database() {
        this.props = new Properties();
        env = ResourceBundle.getBundle("application").getString("env");
    }

    /**
     * Returns the single instance of the Database class, creating it if necessary.
     * This method uses double-checked locking to ensure that only one instance of
     * the class is created in a multi-threaded environment.
     *
     * @return the single instance of the Database class
     * @throws SQLException if an error occurs while creating the database connection
     */
    public static Database getInstance() throws SQLException {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    /**
     * Starts the database connection using the current environment file.
     *
     * @throws SQLException if an error occurs while connecting to the database
     */
    public Connection startDb() {
        dataBaseData = setVarsFromCurrentEnvFile("application-" + env + ".properties");
        return connect();
    }

    /**
     * Loads the properties from the environment file and returns the corresponding
     * `DatabaseEnvData` object.
     *
     * @param envFile the name of the environment file
     * @return the `DatabaseEnvData` object containing the database connection information
     */
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
                props.getProperty("db.inGameRootUser"),
                props.getProperty("db.inGameRootPass")
        );
    }

    /**
     * Sets up the `users` table in the database if it does not already exist.
     *
     * @throws SQLException if an error occurs while executing the SQL statements
     */
    public void setupDbTable() throws SQLException {

        String query = "CREATE DATABASE IF NOT EXISTS " + dataBaseData.getDbName();
        executeUpdate(query);

        query = "USE " + dataBaseData.getDbName();
        executeUpdate(query);

        query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(50) NOT NULL, " +
                "role ENUM(" + Arrays.stream(Roles.values())
                .map(role -> "'" + role.name() + "'")
                .collect(Collectors.joining(","))
                + ") NOT NULL DEFAULT 'PLAYER', " +
                "score INT NOT NULL DEFAULT 0, " +
                "lives INT NOT NULL DEFAULT 3, " +
                "played_games INT NOT NULL DEFAULT 0, " +
                "wins INT NOT NULL DEFAULT 0, " +
                "losses INT NOT NULL DEFAULT 0, " +
                "PRIMARY KEY (id)" +
                ")";
        executeUpdate(query);
        query = String.format("INSERT INTO users (username, password, role) VALUES ('%s', '%s', 'ROOT');",
                dataBaseData.inGameRootUser, dataBaseData.inGameRootPass);
        executeUpdate(query);
    }

    /**
     * Connects to the database using the connection information stored in the
     * `dataBaseData` field.
     *
     * @return the database connection object
     * @throws SQLException if an error occurs while connecting to the database
     */
    private Connection connect() {
        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * Executes a SQL query and returns the result set.
     *
     * @param query the SQL query to execute
     * @return the result set returned by the query, or null if an error occurs
     */
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

    /**
     * Executes a SQL update statement and returns the number of rows affected.
     *
     * @param query the SQL update statement to execute
     * @return the number of rows affected by the update, or -1 if an error occurs
     */
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

    public void dropTable() throws SQLException {
        Statement queryStatement = connection.createStatement();
        queryStatement.executeUpdate(
                "DROP DATABASE IF EXISTS " + dataBaseData.dbName + ";"
        );
    }

    /**
     * Closes the database connection.
     */
    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to close database connection.");
            e.printStackTrace();
        }
    }

    /**
     * Delets current instance of DataBase
     */
    public void closeInstance() {
        close();
        instance = null;
    }
}
