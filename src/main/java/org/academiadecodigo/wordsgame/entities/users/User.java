package org.academiadecodigo.wordsgame.entities.users;

import lombok.Getter;
import lombok.Setter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.academiadecodigo.wordsgame.application.server.ClientDispatch;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.stages.FinishRoom;
import org.academiadecodigo.wordsgame.game.stages.GameRoom;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.game.stages.WaitingRoom;
import org.academiadecodigo.wordsgame.misc.Messages;

@Getter
@Setter
public abstract class User implements Runnable {

    private String userName;
    private ClientDispatch clientDispatch;
    private Socket socket;
    private Stage actualStage;
    private int score;
    private int lives;
    private boolean isReady;
    private boolean readRules;

    public User(String userName, int score, int lives, boolean isReady, ClientDispatch clientDispatch, Socket socket, Stage actualStage, Boolean readRules) {
        this.userName = userName;
        this.score = score;
        this.lives = lives;
        this.isReady = isReady;
        this.clientDispatch = clientDispatch;
        this.socket = socket;
        this.actualStage = actualStage;
        this.readRules = readRules;
    }

    /**
     * Gets the User Input returned by InputStream
     */
    protected String getUserInput() {
        try {
            return  new BufferedReader(new InputStreamReader
                    (getSocket().getInputStream())).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**+
     * Check if this user is currently in this stage
     * @return boolean
     */
    protected boolean isUserInWaitingRoom() {
        return getActualStage() instanceof WaitingRoom;
    }

    /**+
     * Check if this user is currently in this stage
     * @return boolean
     */
    protected boolean isUserInGameRoom() {
        return getActualStage() instanceof GameRoom;
    }

    /**+
     * Check if this user is currently in this stage
     * @return boolean
     */
    protected boolean isUserInFinishSage() {
        return getActualStage() instanceof FinishRoom;
    }

    /**
     * Determines the Behaviour in Certain Stage
     * Stage: Finish Stage
     */
    protected void behaviourInFinishGame() {
        ChatCommandsMessagesTrafficManager.clearScreen(this);
        if( ((FinishRoom) getActualStage()).getWinner() != null) {
            if( ((FinishRoom) getActualStage()).getWinner().equals(this) ) {
                getClientDispatch().notifyPlayer(String.format("CONGRATULATIONS! You are the Winner with %2d score!", getScore()));
                ChatCommandsMessagesTrafficManager.sendMessageToAll(Messages.drawWinner(this.getUserName()));
                return;
            }
        }
        getClientDispatch().notifyPlayer(String.format("You finished the game with a total Score %2d", getScore()));
    }

    /**
     * Determines the Behaviour in Certain Stage
     * Stage: Waiting Room
     */
    protected void behaviourInWaitingRoom() {
        try {
            if (this.getSocket().getInputStream().available() > 0) {
                getActualStage().checkUserInput(this, getUserInput());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the Behaviour in Certain Stage
     * Stage: Game Room
     */
    protected void behaviourInGameRoom() {
        if(!isReadRules()) {
            ChatCommandsMessagesTrafficManager.clearScreen(this);
            getClientDispatch().sendRules();
            setReadRules(true);
        }
        if(getLives() > 0) {
            ChatCommandsMessagesTrafficManager.clearScreen(this);
            getClientDispatch().notifyPlayer(getActualStage().getGrid().drawMatrix());
            getClientDispatch().notifyPlayer(String.format(Messages.getMessage("SHOW_PLAYER_SCORES"), getScore(), getLives()));
            getActualStage().checkUserInput(this, getUserInput());
        } else {
            getActualStage().playerLost(this);
        }
    }

}
