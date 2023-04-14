package misc;

import static org.junit.jupiter.api.Assertions.*;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.database.DatabaseEnvData;
import org.junit.jupiter.api.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        database = Database.getInstance();
        String ENV_TEST = "test";
        database.setEnv(ENV_TEST);
        database.startDb();
    }

    @AfterAll
    public void tearDown() {
        database.closeInstance();
    }

    @Test
    @Order(1)
    public void testSetVarsFromCurrentEnvFile() {
        DatabaseEnvData data = database.setVarsFromCurrentEnvFile();
        assertNotNull(data);
        assertEquals("jdbc:mysql://localhost:3306/wordscrush_test", data.getCompleteUrl());
        assertEquals("root", data.getDbRoot());
        assertEquals("1010", data.getDbRootPass());
        assertEquals("wordscrush_test", data.getDbName());
        assertEquals("sprint", data.getInGameRootUser());
        assertEquals("pass", data.getInGameRootPass());
    }

    @Test
    @Order(2)
    public void testConnect() {
        assertNotNull(database.getConnection());
    }

    @Test
    @Order(3)
    public void testExecuteQuery() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM users";
        ResultSet resultSet = database.executeQuery(query);
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
        int count = resultSet.getInt("total");
        assertTrue(count >= 0);
    }

    @Test
    @Order(4)
    public void testExecuteUpdate() {
        String query = "INSERT INTO users (username, password, role) VALUES ('testuser', 'testpass', 'PLAYER')";
        int rowsAffected = database.executeUpdate(query);
        assertEquals(1, rowsAffected);
    }

    @Test
    @Order(5)
    public void testSetupDbTable() throws SQLException {
        // when
        database.setupDbStructure();

        // then
        // Ensure that the 'users' table exists
        String query = "SHOW TABLES LIKE 'users'";
        ResultSet resultSet = database.executeQuery(query);
        assertTrue(resultSet.next());

        // Ensure that there is an admin user in the 'users' table
        query = "SELECT COUNT(*) AS total FROM users WHERE role = 'ROOT'";
        resultSet = database.executeQuery(query);
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
        int count = resultSet.getInt("total");
        assertTrue(count > 0);
    }

    @Test
    @Order(6)
    public void testDropDatabase() throws SQLException {
        DatabaseEnvData data = database.setVarsFromCurrentEnvFile();

        Statement statement = database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+ data.getDbName() + "';");
        resultSet.next();
        int numRows = resultSet.getInt(1);
        assertEquals(1, numRows);

        // Drop the database
        database.dropTable();

        // Check that the database no longer exists
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + data.getDbName() + "'");
        resultSet.next();
        numRows = resultSet.getInt(1);
        assertEquals(0, numRows);
    }
}
