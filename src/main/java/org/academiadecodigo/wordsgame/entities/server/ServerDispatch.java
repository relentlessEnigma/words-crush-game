package org.academiadecodigo.wordsgame.entities.server;

import org.academiadecodigo.wordsgame.entities.client.Client;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDispatch {

    private CopyOnWriteArrayList<Client> clientsList = new CopyOnWriteArrayList<>();
    private final ExecutorService fixedPool;
    private ServerSocket serverSocket;
    private final int nThreads;
    private int portNumber;
    private String filePath;
    public static Integer MAX_CLIENTS;

    public ServerDispatch(int portNumber, int nThreads, String filePath) {
        this.nThreads = nThreads;
        this.portNumber = portNumber;
        this.fixedPool = Executors.newFixedThreadPool(this.nThreads);
        this.filePath = filePath;
        try {
            serverSocket = new ServerSocket(Integer.valueOf(portNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MAX_CLIENTS = nThreads;
    }

    public void manageNewConnections() throws IOException {

        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.INFO_SERVER_ON);
        ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.INFO_PORT + portNumber);

        //Must have all the players connected to start the game
        while (clientsList.size() < nThreads) {

            Socket clientSocket = serverSocket.accept();
            Client client = new Client(clientSocket, filePath);
            clientsList.add(client);
            ChatCommandsMessagesTrafficManager.sendMessageToServer(Messages.INFO_NEWCONNECTION + clientsList.size());
            fixedPool.submit(client);
        }
    }

}
