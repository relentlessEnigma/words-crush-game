package org.academiadecodigo.wordsgame.misc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static Database database;

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
    public void testConnection() throws SQLException {
        assertNotNull(database.connect());
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        // Arrange
        String query = "SELECT * FROM users WHERE username = 'john' AND password = 'doe'";
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
}
