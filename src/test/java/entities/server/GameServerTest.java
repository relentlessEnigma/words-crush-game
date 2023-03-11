package entities.server;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.client.ClientDispatch;
import org.academiadecodigo.wordsgame.entities.server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

    class GameServerTest {
        private final int PORT_NUMBER = 4212;
        private final int NUM_THREADS = 2;
        private final String FILE_PATH = "src/main/resources/data.txt";
        private GameServer gameServer;

        @Mock
        private ExecutorCompletionService<Void> mockExecutorCompletionService;


        @BeforeEach
        void setUp() throws IOException, SQLException {
            gameServer = null;
            MockitoAnnotations.initMocks(this);
            gameServer = new GameServer(PORT_NUMBER, NUM_THREADS, FILE_PATH);
            gameServer.setExecutorCompletionService(mockExecutorCompletionService);
        }

        @AfterEach
        public void tearDown() throws IOException {
            try {
                if (!gameServer.getServerSocket().isClosed()) {
                    gameServer.getServerSocket().close();
                }
            } catch (IOException e) {
                System.out.println("OOPS!!!");
            }
            if (gameServer != null) {
                gameServer.getServerSocket().close();
                gameServer.close();
            }
            //reset(mockServerSocket);
        }

        @Test
        void testConstructor() {
            assertNotNull(gameServer);
            assertEquals(NUM_THREADS, GameServer.MAX_CLIENTS.intValue());
            assertNotNull(gameServer.getExecutor());
            assertNotNull(gameServer.getServerSocket());
            assertNotNull(gameServer.getFilePath());
            assertNotNull(gameServer.getDb());
            assertEquals(NUM_THREADS, gameServer.getNThreads());
        }

        @Test
        void testClose() throws IOException {
            gameServer.close();

            // Check that the executor is shutdown
            assertTrue(gameServer.getExecutor().isShutdown());
        }
    }
