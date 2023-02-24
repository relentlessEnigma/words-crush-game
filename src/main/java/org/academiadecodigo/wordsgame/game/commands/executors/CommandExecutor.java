package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.misc.Messages;

import java.util.List;

public abstract class CommandExecutor {

    public String execute(String command, User user, List<User> usersList){
        return isApplicable(command) ? executeValidCommand(command, user, usersList) : Messages.get("INFO_INVALID_COMMAND");
    }

    public abstract boolean isApplicable(String command);

    protected abstract String executeValidCommand(String command, User user, List<User> userList);
}
