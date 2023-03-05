package org.academiadecodigo.wordsgame.game.stages;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import java.util.List;

@Getter
@Setter
public class WaitingRoom extends Stage {

    private static WaitingRoom waitingRoom;

    public WaitingRoom(Grid grid, int maxPlayers, List<User> usersInTheRoom) {
        super(grid, maxPlayers, usersInTheRoom);
    }

    public static WaitingRoom getInstance(Grid grid, int maxPlayers, List<User> usersInTheRoom){
        return (waitingRoom == null) ? waitingRoom = new WaitingRoom(grid, maxPlayers, usersInTheRoom) : waitingRoom;
    }

    /**
     * Check if all players are ready
     * @return boolean
     */
    private boolean arePlayersReady(){
        return getUsersInTheRoom().size() > 0 && getUsersInTheRoom().stream().allMatch(User::isReady);
    }

    /**
     * Check user Input
     * @param user
     * @param message
     */
    @Override
    public synchronized void checkUserInput(User user, String message) {

        if (message.startsWith("/")) {
            user.getClientDispatch().notifyPlayer(getCommandRunner().runCommand(message, user, getUsersInTheRoom()));
            return;
        }
        ChatCommandsMessagesTrafficManager.sendMessageToChat(user, message);
    }

    /**
     * Add new User to this stage
     * @param user
     */
    public void registerUserInStage(User user) {
        this.getUsersInTheRoom().add(user);
    }

    @Override
    public void run() {

        while(!arePlayersReady()) {}

        Stage nextStage = ChangeStage.changeToGameRoomStage(this);
        Thread t = new Thread(nextStage);
        t.start();
    }
}
