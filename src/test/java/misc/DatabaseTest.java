package misc;

import org.academiadecodigo.wordsgame.misc.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static Database database;
    private String testDb = "wordscrush_test";

    @BeforeAll
    public static void setUp() throws SQLException {
        database = new Database();
        database.connect();
    }

    @AfterAll
    public static void tearDown() {
        database.close();
    }

    @Test
    public void testConnect() throws SQLException {
        Connection connection = database.connect();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        // Arrange
        String query = "USE " + testDb + ";";
        database.executeUpdate(query);
        query = "SELECT * FROM users WHERE username = 'john' AND password = 'doe'";
        // Act
        ResultSet result = database.executeQuery(query);

        // Assert
        assertTrue(result.next()); // Check if the result set is not empty
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        int rowsAffected = database.executeUpdate("UPDATE users SET username = 'Jane' WHERE id = 1");
        assertEquals(1, rowsAffected);
        ResultSet resultSet = database.executeQuery("SELECT * FROM users WHERE id = 1");
        assertNotNull(resultSet);
        resultSet.next();
        assertEquals("Jane", resultSet.getString("username"));
        database.executeUpdate("UPDATE users SET username = 'john' WHERE id = 1"); //make it default again
    }

    @Test
    public void testSetupDbTable() throws SQLException {
        // Test for dev environment
        String dbName = "wordscrush_dev";
        Connection connection = database.connect();
        assertFalse(connection.isClosed());

        // Drop the database if it already exists
        String dropDatabaseQuery = "DROP DATABASE IF EXISTS " + dbName;
        database.executeUpdate(dropDatabaseQuery);

        database.setupDbTable();

        // Check if the database and table were created
        String checkDatabaseQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        PreparedStatement checkDatabaseStmt = connection.prepareStatement(checkDatabaseQuery);
        checkDatabaseStmt.setString(1, dbName);
        ResultSet resultSet = checkDatabaseStmt.executeQuery();
        assertTrue(resultSet.next());

        String checkTableQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        PreparedStatement checkTableStmt = connection.prepareStatement(checkTableQuery);
        checkTableStmt.setString(1, dbName);
        checkTableStmt.setString(2, "users");
        resultSet = checkTableStmt.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt(1));

        // Check if the mockup data was inserted
        String checkDataQuery = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ? AND role = ?";
        PreparedStatement checkDataStmt = connection.prepareStatement(checkDataQuery);
        checkDataStmt.setString(1, "admin");
        checkDataStmt.setString(2, "pass");
        checkDataStmt.setString(3, "ADMIN");
        resultSet = checkDataStmt.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt(1));

        connection.close();
    }
}
