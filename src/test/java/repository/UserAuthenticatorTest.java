package repository;

import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.database.DatabaseEnvData;
import org.academiadecodigo.wordsgame.entities.users.Roles;
import org.academiadecodigo.wordsgame.repository.UserAuthenticator;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAuthenticatorTest {

    private Database database;
    private final String ENV_TEST = "test";

    private DatabaseEnvData dataBaseData;

    @BeforeAll
    public void setUp() throws SQLException {
        database = Database.getInstance();
        database.setEnv(ENV_TEST);
        database.startDb();
        assertNotNull(database);

        dataBaseData = database.getDataBaseData();
    }

    @AfterAll
    public void closeConnection() {
        database.closeInstance();
    }

    @Test
    public void testAuthenticateRoot() {
        boolean result = UserAuthenticator.authenticateRoot(dataBaseData.getInGameRootUser(), dataBaseData.getInGameRootPass());
        assertTrue(result);
    }

    @Test
    public void testRegister() {
        // Test the register() method
        String mockAdmin = "mock_Admin";
        UserAuthenticator.register(Roles.ADMIN, mockAdmin, "mock_password");
        assertEquals("ADMIN", Objects.requireNonNull(UserAuthenticator.getUserRole(mockAdmin)).toString());
    }

    @Test
    public void testRegisterAdmin() {
        // Authenticate as ROOT user
        boolean authenticated = UserAuthenticator.authenticateRoot(dataBaseData.getInGameRootUser(), dataBaseData.getInGameRootPass());
        assertTrue("Failed to authenticate as ROOT user", authenticated);

        // If authenticated, register new admin user
        if (authenticated) {
            UserAuthenticator.register(Roles.ADMIN, "newadmin", "newadminpassword");

            // Confirm that the new admin user can log in
            boolean loggedIn = UserAuthenticator.login("newadmin", "newadminpassword");
            assertTrue("Failed to log in as new admin user", loggedIn);

            // Confirm that the new admin user has the ADMIN role
            Roles role = UserAuthenticator.getUserRole("newadmin");
            assertEquals("New admin user does not have the ADMIN role", Roles.ADMIN, role);
        }
    }

    @Test
    public void testLogin() {
        // Test the login() method
        assertTrue(UserAuthenticator.login("sprint", "pass"));
        assertFalse(UserAuthenticator.login("admin", "wrongpassword"));
    }

    @Test
    public void testGetUserRole() {
        // Test the getUserRole() method
        assertEquals(Roles.ROOT, UserAuthenticator.getUserRole(dataBaseData.getInGameRootUser()));
        assertNull(UserAuthenticator.getUserRole("nonexistentuser"));
    }

}
