package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.util.List;

public class ListCommandExecutor extends CommandExecutor{

    public static final String LIST_COMMAND = CommandsList.LIST.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(LIST_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> usersList) {

        StringBuilder sb = new StringBuilder();
        sb.append(Messages.INFO_LIST_PLAYERS);

        for (int i = 0; i < usersList.size(); i++) {
            sb.append(String.format("> %s (%s)\n", usersList.get(i).getUserName(), usersList.get(i).isReady()));
        }
        ChatCommandsMessagesTrafficManager.sendMessageToServer(String.format(Messages.INFO_SOMEONE_IS_WATCHING_LIST, user.getUserName()));

        return sb.toString();
    }
}
