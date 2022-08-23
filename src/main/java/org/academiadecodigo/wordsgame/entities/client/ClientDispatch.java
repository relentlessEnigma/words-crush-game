package org.academiadecodigo.wordsgame.entities.client;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.io.*;
import java.net.Socket;

@Getter
@Setter
public class ClientDispatch {

    private ChatCommandsMessagesTrafficManager chat;
    private Socket socket;
    private InputStream inStream;
    private Stage actualStage;
    private PrintWriter outStream;

    public ClientDispatch(Socket socket, Stage actualstage) {
        this.actualStage = actualstage;
        this.chat = new ChatCommandsMessagesTrafficManager();
        this.socket = socket;
        try {
            this.outStream = new PrintWriter(socket.getOutputStream(), true);
            this.inStream = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the rules of the game to all users
     * through User's personal CP (Client Dispatch)
     */
    public void sendRules(User user) {

        user.getClientDispatch().getOutStream().println(Messages.ART_GAME_RULES);
        user.getClientDispatch().getOutStream().println(Messages.INFO_STARTING_GAME_IN);

        for (int i = 10; i >= 0; i--) {
            try {
                Thread.sleep(0); //TODO: correct this to 1000 mss again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i != 0) {
                user.getClientDispatch().getOutStream().println(i + " ");
            }
        }
    }

    /**
     * Generates personalized message to self player.
     * @param message to be sent.
     * @param clientObj to whom.
     */
    public void notifyPlayer(String message, User clientObj) {

        for (User user : actualStage.getUsersInTheRoom()) {
            if (user.equals(clientObj)) {
                user.getClientDispatch().getOutStream().println(message);
            }
        }
    }

}
