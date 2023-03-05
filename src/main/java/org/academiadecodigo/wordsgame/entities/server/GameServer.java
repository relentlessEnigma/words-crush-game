package org.academiadecodigo.wordsgame.entities.server;

import org.academiadecodigo.wordsgame.entities.client.ClientDispatch;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class GameServer {

    public static Integer MAX_CLIENTS;
    private final ExecutorService executor;
    private final ExecutorCompletionService<Void> executorCompletionService;
    private final ServerSocket serverSocket;
    private final String filePath;
    private int nThreads;

    public GameServer(int portNumber, int nThreads, String filePath) throws IOException {
        this.nThreads = nThreads;
        this.filePath = filePath;
        this.MAX_CLIENTS = nThreads;

        executor = Executors.newFixedThreadPool(nThreads);
        executorCompletionService = new ExecutorCompletionService<>(executor);
        serverSocket = new ServerSocket(portNumber);

        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.get("INFO_SERVER_ON"));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.get("INFO_PORT") + portNumber);
    }

    public void manageNewConnections() {
        int numClients = 0;
        while (numClients < nThreads) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientDispatch clientDispatch = new ClientDispatch(clientSocket, filePath);
                executorCompletionService.submit(clientDispatch, null);
                numClients++;
                ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.get("INFO_NEWCONNECTION") + numClients);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        executor.shutdownNow();
    }
}
