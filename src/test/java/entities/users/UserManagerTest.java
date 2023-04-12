package entities.users;

import org.academiadecodigo.bootcamp.InputScanner;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.Role;
import org.academiadecodigo.wordsgame.entities.users.UserManager;
import org.academiadecodigo.wordsgame.repository.UserAuthenticator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    private Database database;
    private final String ENV_TEST = "test";
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintWriter printWriter = new PrintWriter(out, true);

    @Mock
    private Prompt mockPrompt;

    public void setUp() {
        try {
            database = Database.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        database.setEnv(ENV_TEST);
        database.startDb();
    }

    @BeforeEach
    public void setUpEach() {
        setUp();
        out.reset();
    }

//    @AfterAll
//    public void closeAll() {
//        database.closeInstance();
//    }

    @Test
    @Order(1)
    void registerNewAdminTest() {
        // given
        String inGameRootUser = database.getDataBaseData().getInGameRootUser();
        String inGameRootPass = database.getDataBaseData().getInGameRootPass();

        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn(1, inGameRootUser, inGameRootPass, "admin", "admin");
        UserManager userManager = new UserManager(printWriter, mockPrompt);
        UserAuthenticator.authenticateRoot(inGameRootUser, inGameRootPass);

        // when
        userManager.register();

        // then
        String expected = "A new Admin Account was configured" + System.lineSeparator();
        assertEquals(Role.ADMIN, UserAuthenticator.getUserRole("admin"));
        assertEquals(expected, out.toString());
    }

    @Test
    @Order(2)
    void registerNewPlayerTest() {
        // given
        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn(2, "player1", "player1234");
        UserManager userManager = new UserManager(printWriter, mockPrompt);

        // when
        userManager.register();

        // then
        assertEquals(Role.PLAYER, UserAuthenticator.getUserRole("player1"));
    }

    @Test
    @Order(3)
    void loginTest() {
        // given
        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn("player1", "player1234");
        UserManager userManager = new UserManager(printWriter, mockPrompt);

        // when
        userManager.login();

        // then
        String expected = "Login with your details:" + System.lineSeparator() + "Welcome back player1" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }
}
