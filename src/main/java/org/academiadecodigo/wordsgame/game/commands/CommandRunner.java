package org.academiadecodigo.wordsgame.game.commands;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.commands.executors.CommandExecutor;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.util.List;

public class CommandRunner {

    List<CommandExecutor> commandExecutors;

    public CommandRunner(List<CommandExecutor> commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    public String runCommand(String command, User user, List<User> usersList){

        if (command.isBlank() || command.isEmpty()) return Messages.get("INFO_INVALIDBLANKS");

        for(CommandExecutor executor : commandExecutors) {
            if(executor.isApplicable(command)) {
                return executor.execute(command, user, usersList);
            }
        }

        return Messages.get("INFO_INVALID_COMMAND");
    }
}
