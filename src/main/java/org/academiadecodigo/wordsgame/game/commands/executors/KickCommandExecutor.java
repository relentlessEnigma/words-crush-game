package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.Admin;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.util.List;

public class KickCommandExecutor extends CommandExecutor{

    public static final String KICK_COMMAND = CommandsList.KICK.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(KICK_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> userList) {
        return (user instanceof Admin) ? ChatCommandsMessagesTrafficManager.commandKick(user) : Messages.INFO_INSUFFICIENT_ADMIN_RIGHTS;
    }
}
