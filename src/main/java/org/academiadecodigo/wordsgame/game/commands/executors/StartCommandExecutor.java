package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import java.util.List;

public class StartCommandExecutor extends CommandExecutor{

    public static final String START_COMMAND = CommandsList.START.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(START_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> userList) {
        user.setReady(true);
        return ChatCommandsMessagesTrafficManager.commandStart();
    }


}

