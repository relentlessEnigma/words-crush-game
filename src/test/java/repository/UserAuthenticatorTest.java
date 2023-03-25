package repository;

import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.Roles;
import org.academiadecodigo.wordsgame.repository.UserAuthenticator;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAuthenticatorTest {

    private Database database;
    private final String ENV_TEST = "test";

    @BeforeAll
    public void setUp() throws SQLException {
        Database.getInstance().setEnv(ENV_TEST);
        Database.getInstance().startDb();
        database.startDb();
    }

    @AfterAll
    public void tearDown() {
        database.closeInstance();
    }

    @Test
    public void testAuthenticateRoot() {
        // Test the authenticateRoot() method
        assertFalse(UserAuthenticator.authenticateRoot("root", "Wrongpassword"));
        assertTrue(UserAuthenticator.authenticateRoot("sprint", "pass"));
        assertFalse(UserAuthenticator.authenticateRoot("root", "wrongpassword"));
    }

//    @Test
//    public void testRegister() {
//        // Test the register() method
//        UserAuthenticator.register(Roles.ADMIN, "admin", "password");
//        assertEquals("ADMIN", UserAuthenticator.getUserRole("admin"));
//    }
//
//    @Test
//    public void testLogin() {
//        // Test the login() method
//        assertTrue(UserAuthenticator.login("sprint", "pass"));
//        assertFalse(UserAuthenticator.login("admin", "wrongpassword"));
//    }
//
//    @Test
//    public void testGetUserRole() {
//        // Test the getUserRole() method
//        assertEquals("ROOT", UserAuthenticator.getUserRole("sprint"));
//        assertNull(UserAuthenticator.getUserRole("nonexistentuser"));
//    }
}
