package org.academiadecodigo.wordsgame.game.stages;

import lombok.Getter;
import org.academiadecodigo.wordsgame.entities.server.GameServer;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.game.grid.server.ServerGrid;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.util.List;

@Getter
public class GameRoom extends Stage {

    private ServerGrid sg;
    private Stage finishStage;
    private boolean gameIsFinished = false;

    public GameRoom(Grid grid, int maxPlayers, List<User> usersInTheRoom) {
        super(grid, maxPlayers, usersInTheRoom);
        this.sg = new ServerGrid();
        ChatCommandsMessagesTrafficManager.sendMessageToAll(Messages.get("SET_YOURSELF_READY"));
    }

    public void startStage() {
        setupGrid();
    }

    @Override
    public void checkUserInput(User user, String message) {

        int score = getGrid().checkPlayerInput(message);
        if(score > 0) {
            user.setScore(user.getScore()+score);
            return;
        }
        user.setLives(user.getLives()-1);
    }

    /**
     * Setup the grid with the words
     */
    public void setupGrid() {
        getGrid().setWordsForMatrix();
    }

    @Override
    public void run() {

        startStage();
        this.finishStage = ChangeStage.setFinishStage(this);
        ChatCommandsMessagesTrafficManager.clearScreenServerSide();
    }

    public synchronized void playerLost(User user){
        ((FinishRoom) finishStage).addUserToStage(user);
        if(finishStage.getUsersInTheRoom().size() == GameServer.MAX_CLIENTS-1) checkForTheWinner();
    }

    private void checkForTheWinner() {
        ((FinishRoom)finishStage).setWinner(getWinner());
    }

    private User getWinner() {

        for(User u : this.getUsersInTheRoom()) {
            if(!finishStage.getUsersInTheRoom().contains(u)) {
                gameIsFinished = true;
                return u;
            }
        }
        return null;
    }


}
