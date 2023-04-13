package org.academiadecodigo.wordsgame.application.server;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.*;

@Getter
@Setter
public class GameServer {

    public static Integer MAX_CLIENTS;
    private ExecutorService executor;
    private ExecutorCompletionService<Void> executorCompletionService;
    private ServerSocket serverSocket;
    private final String filePath;
    private int nThreads;
    private Database db;

    public GameServer(int portNumber, int nThreads, String filePath) throws IOException, SQLException {
        this.db = Database.getInstance();
        this.nThreads = nThreads;
        this.filePath = filePath;
        MAX_CLIENTS = nThreads;

        executor = Executors.newFixedThreadPool(nThreads);
        executorCompletionService = new ExecutorCompletionService<>(executor);
        serverSocket = new ServerSocket(portNumber);

        this.db.startDb();

        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.getMessage("INFO_SERVER_ON"));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.getMessage("INFO_PORT") + portNumber);
    }

    public void manageNewConnections() {
        int numClients = 0;
        while (numClients < nThreads) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientDispatch clientDispatch = new ClientDispatch(clientSocket, filePath);
                executorCompletionService.submit(clientDispatch, null);
                numClients++;
                ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.getMessage("INFO_NEWCONNECTION") + numClients);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        System.out.println("Closing server socket...");
        serverSocket.close();
        executor.shutdownNow();
    }
}
