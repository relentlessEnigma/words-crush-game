package org.academiadecodigo.wordsgame.entities.users;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.application.server.ClientDispatch;
import java.net.Socket;

@Getter
@Setter
public class Admin extends User implements Runnable {

    public Admin(int id, String userName, int score, int lives, boolean isReady, ClientDispatch clientDispatch, Socket socket, Stage actualStage, Boolean isReadyConfirmed) {
        super(id, userName, score, lives, isReady, clientDispatch, socket, actualStage, isReadyConfirmed);
    }

    @Override
    public void run() {

        while(isUserInWaitingRoom()) {
            behaviourInWaitingRoom();
        }

        try {
            Thread.sleep(1000); //Wait 1 sec so the grid has time to setup.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(isUserInGameRoom()){
            behaviourInGameRoom();
        }

        if(isUserInFinishSage()) {
            behaviourInFinishGame();
        }
    }
}
