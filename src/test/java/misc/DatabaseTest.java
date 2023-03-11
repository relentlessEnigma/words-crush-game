package misc;

import org.academiadecodigo.wordsgame.repository.Database;
import org.academiadecodigo.wordsgame.repository.DatabaseEnvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;

public class DatabaseTest {

    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        database = null;
        database = new Database();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testSetVarsFromCurrentEnvFile() {
        String envFile = "application-test.properties";
        DatabaseEnvData data = database.setVarsFromCurrentEnvFile(envFile);
        assertNotNull(data);
        assertEquals("jdbc:mysql://localhost:3306/wordscrush_test", data.getCompleteUrl());
        assertEquals("root", data.getDbRoot());
        assertEquals("mysqlpw", data.getDbRootPass());
        assertEquals("wordscrush_test", data.getDbName());
        assertEquals("test", data.getGameRoot());
        assertEquals("test", data.getGameRootPass());
    }

    @Test
    public void testConnect() {
        assertNotNull(database.getConnection());
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM users";
        ResultSet resultSet = database.executeQuery(query);
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
        int count = resultSet.getInt("total");
        assertTrue(count >= 0);
    }

    @Test
    public void testExecuteUpdate() {
        String query = "INSERT INTO users (username, password, role) VALUES ('testuser', 'testpass', 'USER')";
        int rowsAffected = database.executeUpdate(query);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testSetupDbTable() throws SQLException {
        // Ensure that the 'users' table exists
        String query = "SHOW TABLES LIKE 'users'";
        ResultSet resultSet = database.executeQuery(query);
        assertTrue(resultSet.next());

        // Ensure that there is an admin user in the 'users' table
        query = "SELECT COUNT(*) AS total FROM users WHERE role = 'ADMIN'";
        resultSet = database.executeQuery(query);
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
        int count = resultSet.getInt("total");
        assertTrue(count > 0);
    }
}
