package entities.server;

import org.academiadecodigo.wordsgame.entities.client.ClientDispatch;
import org.academiadecodigo.wordsgame.entities.server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.ExecutorCompletionService;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

class GameServerTest {
    private final int PORT_NUMBER = 8015;
    private final int NUM_THREADS = 4;
    private final String FILE_PATH = "src/main/resources/data.txt";
    private GameServer gameServer;

    @Mock
    private ExecutorCompletionService<Void> mockExecutorCompletionService;

    @Mock
    private ServerSocket mockServerSocket;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        MockitoAnnotations.initMocks(this);
        gameServer = new GameServer(PORT_NUMBER, NUM_THREADS, FILE_PATH);
        gameServer.setServerSocket(mockServerSocket);
        gameServer.setExecutorCompletionService(mockExecutorCompletionService);
    }

    @AfterEach
    public void tearDown() throws IOException {
        try {
            if (mockServerSocket != null && !mockServerSocket.isClosed()) {
                mockServerSocket.close();
            }
        } catch (IOException e) {
            System.out.println("OOPS!!!");
        }
        gameServer.getServerSocket().close();
        reset(mockServerSocket);
        gameServer.close();
    }

    @Test
    void testConstructor() {
        assertNotNull(gameServer);
        assertEquals(NUM_THREADS, GameServer.MAX_CLIENTS.intValue());
        assertNotNull(gameServer.getExecutor());
        assertNotNull(gameServer.getServerSocket());
        assertNotNull(gameServer.getFilePath());
        assertNotNull(gameServer.getDb());
        assertEquals(NUM_THREADS, gameServer.getnThreads());
    }

    @Test
    void testManageNewConnections() throws Exception {
        // Mock the client sockets and the ClientDispatch tasks
        Socket mockClientSocket1 = mock(Socket.class);
        Socket mockClientSocket2 = mock(Socket.class);
        ClientDispatch mockClientDispatch1 = mock(ClientDispatch.class);
        ClientDispatch mockClientDispatch2 = mock(ClientDispatch.class);
        when(mockServerSocket.accept()).thenReturn(mockClientSocket1, mockClientSocket2);
        whenNew(ClientDispatch.class).withArguments(mockClientSocket1, FILE_PATH).thenReturn(mockClientDispatch1);
        whenNew(ClientDispatch.class).withArguments(mockClientSocket2, FILE_PATH).thenReturn(mockClientDispatch2);

        gameServer.manageNewConnections();

        // Check that the server socket is not closed
        verify(mockServerSocket, never()).close();

        // Check that the executor completion service has tasks
        verify(mockExecutorCompletionService, times(2)).submit(any(ClientDispatch.class), eq(null));
    }

    @Test
    void testClose() throws IOException {
        gameServer.close();

        // Check that the server socket is closed
        verify(mockServerSocket, times(1)).close();

        // Check that the executor is shutdown
        assertTrue(gameServer.getExecutor().isShutdown());
    }
}
