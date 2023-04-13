package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.Admin;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.util.List;

public class StartAllCommandExecutor extends CommandExecutor{

    public static final String START_ALL_COMMAND = CommandsList.STARTALL.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(START_ALL_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> userList) {
        return (user instanceof Admin) ?
                setAllPlayersReady(userList) :
                Messages.getMessage("INFO_INSUFFICIENT_ADMIN_RIGHTS");
    }

    /**
     * Set All Players in Game Ready
     * @param userList
     * @return String
     */
    private String setAllPlayersReady(List<User> userList) {
        userList.forEach(user -> user.setReady(true));
        return ChatCommandsMessagesTrafficManager.commandStartAll();
    }
}
