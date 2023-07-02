package org.academiadecodigo.wordsgame.game.commands.executors;

import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import org.academiadecodigo.wordsgame.misc.Colors;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PmCommandExecutor extends CommandExecutor{

    public static final String PM_COMMAND = CommandsList.PM.getCommand();

    @Override
    public boolean isApplicable(String command) {
        return command.equals(PM_COMMAND);
    }

    @Override
    protected String executeValidCommand(String command, User user, List<User> usersList) {

        if(usersList.size() == 1) {
            return Messages.getMessage("ERROR_NOT_ENOUGH_PLAYERS_IN_ROOM");
        }
        String[] strArray = new String[removeUserFromList(user, usersList).size()];
        StringInputScanner personalMessage = new StringInputScanner();

        for (int i = 0; i < removeUserFromList(user, usersList).size(); i++) {
            strArray[i] = removeUserFromList(user, usersList).get(i).getUserName();
        }

        User userDestiny = removeUserFromList(user, usersList)
                .get(new PromptMenu<Integer>().createNewMenu(strArray, Messages.getMessage("INFO_USERS_AVAILABLE"), ChatCommandsMessagesTrafficManager.getUserPrompt(user))-1);

        personalMessage.setMessage(Messages.getMessage("SEND_MESSAGE_TO_PLAYER"));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(String.format(Messages.getMessage("PLAYER_SENT_PM_TO_OTHER"), user.getUserName(), userDestiny.getUserName(), Colors.PURPLE_BOLD_BRIGHT, Colors.RESET));
        userDestiny.getClientDispatch().notifyPlayer(String.format(Messages.getMessage("PLAYER_MESSAGE_FOR_PM"), user.getUserName(), ChatCommandsMessagesTrafficManager.getUserPrompt(user).getUserInput(personalMessage)));
        
        return ChatCommandsMessagesTrafficManager.sendPrivateMessageToUser();
    }

    /**
     * Removes one determined user from a previous defined list
     * @param originUser
     * @return List<User>
     */
    private static List<User> removeUserFromList(@NotNull User originUser, List<User> usersList) {

        List<User> finalList = new CopyOnWriteArrayList<>();
        for(User u : usersList) {
            if(!u.equals(originUser)) finalList.add(u);
        }
        return finalList;
    }
}
