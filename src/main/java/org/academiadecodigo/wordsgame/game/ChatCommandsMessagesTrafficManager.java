package org.academiadecodigo.wordsgame.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.users.Player;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.commands.executors.list.CommandsList;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class will manage all the traffic messages
 * that is happening in the server.
 * From here, will generate personalized messages.
 * The messages will then be dispatched to the server by User's personal Client Dispatch
 * This does not send messages to the server or users.
 */
public class ChatCommandsMessagesTrafficManager {

    /**
     * Keep all the users in List
     */
    private static List<User> usersList = new CopyOnWriteArrayList<>();

    /**
     * Adds new user to the list.
     * @param user
     */
    public static void registerUserForChatsManagement(User user) {
        usersList.add(user);
    }


    /**
     * Send message to all users in game
     * @param message
     */
    public synchronized static void sendMessageToAll(String message) {

        for(User user: usersList){
            user.getClientDispatch().getOutStream().println(message);
        }
    }

    /**
     * Method to clear user's side Screen
     */
    public synchronized static void clearScreen(User user) {
        user.getClientDispatch().notifyPlayer(Messages.getMessage("ART_CLEAR_SCREEN"));
        user.getClientDispatch().notifyPlayer(Messages.getMessage("ART_GAME_TITLE"));
    }

    /**
     * Clears the server side screen
     */
    public static void clearScreenServerSide() {
        sendMessageToServer(Messages.getMessage("ART_CLEAR_SCREEN"));
        sendMessageToServer(Messages.getMessage("ART_GAME_TITLE"));
    }

    /**
     * Dispatches a message to server side
     * @param message
     */
    public static void sendMessageToServer(String message) {
        System.out.println(message);
    }

    /**
     * Removes one determined user from a previous defined list
     * @param originUser
     * @return List<User>
     */
    private static List<User> removeUserFromList(@NotNull User originUser) {

        List<User> finalList = new CopyOnWriteArrayList<>();
        for(User u : usersList) {
            if(!u.equals(originUser)) finalList.add(u);
        }
        return finalList;
    }

    /**
     * Returns confirmation that PM was sent to player
     * @return String
     */
    public static String sendPrivateMessageToUser() {
        return Messages.getMessage("INFO_PM_SENT");
    }

    /**
     * Dispatch message to chat
     * @param userOrigin
     * @param message
     */
    public static void sendMessageToChat(User userOrigin, String message) {

        for(User u : usersList) {
            if(!u.equals(userOrigin)) {
                u.getClientDispatch().notifyPlayer(String.format("%s: %s", userOrigin.getUserName(),message));
            }
        }
    }

    /**
     * Returns a String confirming one player was set to start
     * @return String
     */
    public static String commandStart() {
        return Messages.getMessage("INFO_PLAYER_READY");
    }

    /**
     * Returns String confirming All players were set ready to start
     * @return String
     */
    public static String commandStartAll() {
        return Messages.getMessage("INFO_ALL_PLAYERS_READY");
    }

    /**
     * Creates a prompt for a user
     * @param user
     * @return Prompt
     */
    public static Prompt getUserPrompt(User user) {
        try {
            PrintStream printStream = new PrintStream(user.getClientDispatch().getSocket().getOutputStream());
            return new Prompt(user.getClientDispatch().getSocket().getInputStream(), printStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kick a Selected Player.
     * Creates a list with available players.
     * @param originUser
     * @return String
     */
    public static String commandKick(User originUser) {
        String[] strArray = new String[removeUserFromList(originUser).size()];

        for (int i = 0; i < removeUserFromList(originUser).size(); i++) {
            strArray[i] = removeUserFromList(originUser).get(i).getUserName();
        }

        User userDestiny = removeUserFromList(originUser)
                .get(new PromptMenu<Integer>()
                        .createNewMenu(strArray, Messages.getMessage("QUESTION_SELECT_PLAYER_TO_KICK"), getUserPrompt(originUser))-1);

        String kickReason = new PromptMenu<String>().createNewQuestion(Messages.getMessage("DEFINE_KICK_MESSAGE"), getUserPrompt(originUser));
        userDestiny.getClientDispatch().notifyPlayer(String.format(Messages.getMessage("PLAYER_MESSAGE_FOR_KICK"), kickReason));
        sendMessageToServer(String.format(Messages.getMessage("INFO_PLAYER_KICKED"), userDestiny.getUserName(), kickReason));
        if(userDestiny instanceof Player) ((Player) userDestiny).kick();

        return Messages.getMessage("YOU_KICKED_A_PLAYER");
    }

    /**
     * Shows available Commands
     * @return String
     */
    public static String commandHelp() {
        return "List Of Commands: " + List.of(CommandsList.values());
    }
}