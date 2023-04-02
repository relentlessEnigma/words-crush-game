package entities.users;

import org.academiadecodigo.bootcamp.InputScanner;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.Roles;
import org.academiadecodigo.wordsgame.entities.users.UserManager;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.academiadecodigo.wordsgame.repository.UserAuthenticator;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagerTest {

    private Database database;
    private final String ENV_TEST = "test";

    @Mock
    private Prompt mockPrompt;

    @Mock
    private PromptMenu mockPromptMenu;

    public final ByteArrayOutputStream out = new ByteArrayOutputStream();
    public final PrintWriter printWriter = new PrintWriter(out, true);



    @BeforeAll
    public void setUp() throws SQLException {
        database = Database.getInstance();
        database.setEnv(ENV_TEST);
        database.startDb();
    }

    @BeforeEach
    public void setUpEach() {
        out.reset();
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public void closeAll() {
        database.closeInstance();
    }


    @Test
    void registerNewAdminTest() {
        // given
        String inGameRootUser = database.getDataBaseData().getInGameRootUser();
        String inGameRootPass = database.getDataBaseData().getInGameRootPass();

        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn(1, inGameRootUser, inGameRootPass, "admin", "admin");
        when(mockPromptMenu.createNewMenu(any(String[].class), any(String.class), any(Prompt.class))).thenReturn(1);
        when(mockPromptMenu.createNewQuestion(eq("Login with root privileges first.\nRootName: "), any(Prompt.class))).thenReturn(inGameRootUser);
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_PASSWORD")), any(Prompt.class))).thenReturn(inGameRootPass);
        when(mockPromptMenu.createNewQuestion(eq("Set Admin Name: "), any(Prompt.class))).thenReturn("admin");
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_PASSWORD")), any(Prompt.class))).thenReturn("admin");

        UserManager userManager = new UserManager(printWriter, mockPrompt);
        UserAuthenticator.authenticateRoot(inGameRootUser, inGameRootPass);

        // when
        userManager.register();

        // then
        String expected = "A new Admin Account was configured" + System.lineSeparator();
        assertEquals(Roles.ADMIN, UserAuthenticator.getUserRole("admin"));
        assertEquals(expected, out.toString());
    }

    @Test
    void registerNewPlayerTest() {
        // given
        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn(2, "player1", "player1234");
        when(mockPromptMenu.createNewMenu(any(String[].class), any(String.class), any(Prompt.class))).thenReturn(2);
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_NICKNAME")), any(Prompt.class))).thenReturn("player1");
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_PASSWORD")), any(Prompt.class))).thenReturn("player1234");

        UserManager userManager = new UserManager(printWriter, mockPrompt);

        // when
        userManager.register();

        // then
        assertEquals(Roles.PLAYER, UserAuthenticator.getUserRole("player1"));
    }

    @Test
    void loginTest() {
        // given
        when(mockPrompt.getUserInput(any(InputScanner.class))).thenReturn("player1", "player1234");
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_NICKNAME")), any(Prompt.class))).thenReturn("player1");
        when(mockPromptMenu.createNewQuestion(eq(Messages.get("INFO_SET_PASSWORD")), any(Prompt.class))).thenReturn("player1234");

        UserManager userManager = new UserManager(printWriter, mockPrompt);

        // when
        userManager.login();

        // then
        String expected = "Login with your details:" + System.lineSeparator() + "Welcome back player1" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }
}
