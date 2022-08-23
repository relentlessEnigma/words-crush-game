package org.academiadecodigo.wordsgame.game.stages;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChangeStage  {

    private static Stage finishStage = null;

    /**
     * Change to Room Stage
     * Set all Player's ActualStage to this new stage
     * @param oldStage
     * @return Stage
     */
    protected static Stage changeToGameRoomStage(Stage oldStage) {
        Stage newStage = new GameRoom(oldStage.getGrid(), oldStage.getMaxPlayers(), oldStage.getUsersInTheRoom());
        newStage.getUsersInTheRoom().forEach(user -> user.setActualStage(newStage));
        return newStage;
    }

    /**
     * Creates a new Finish Stage if null
     * @param oldStage
     * @return Stage
     */
    protected static Stage setFinishStage(Stage oldStage) {
        if(finishStage == null) {
            return finishStage = new FinishRoom(oldStage.getGrid(), oldStage.getMaxPlayers(), new CopyOnWriteArrayList<>());
        }
        return finishStage;
    }

}
