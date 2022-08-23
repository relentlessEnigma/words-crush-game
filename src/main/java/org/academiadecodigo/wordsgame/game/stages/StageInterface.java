package org.academiadecodigo.wordsgame.game.stages;

import org.academiadecodigo.wordsgame.entities.users.User;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface StageInterface extends Runnable {

    void checkUserInput(User user, String message);
    List<User> getUsersInTheRoom();
}
