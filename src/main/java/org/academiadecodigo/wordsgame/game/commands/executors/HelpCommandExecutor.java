package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;

import java.util.List;

public class HelpCommandExecutor extends CommandExecutor{

    public static final String HELP_COMMAND = CommandsList.HELP.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(HELP_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> userList) {
        return ChatCommandsMessagesTrafficManager.commandHelp();
    }
}
