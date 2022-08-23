package org.academiadecodigo.wordsgame.misc;

import java.util.Properties;

public abstract class Messages {

    private static Properties properties = new Properties();

    public static final String INFO_SET_NICKNAME = "[INFO]: Set your nickname or login: ";
    public static final String INFO_NEWCONNECTION = "[SERVER]: New Client connected: ";
    public static final String INFO_SERVER_ON = "[INFO]: SERVER IS ONLINE - Waiting For playing connections";
    public static final String INFO_PORT = "[INFO]: PORT: ";
    public static final String INPUT_ADMIN_PASSWORD = "[SERVER]: Insert Password: ";
    public static final String INFO_INVALIDBLANKS = "[SERVER]: Blank Message not valid!";
    public static final String INFO_STARTING_GAME_IN = String.format("%sStarting the game in: %s", Colors.RED_BOLD, Colors.RESET);
    public static final String INFO_USERS_AVAILABLE = "[SERVER] Users Available: ";
    public static final String INFO_INVALID_COMMAND = "[INFO] Invalid Command!";
    public static final String INFO_PLAYER_KICKED = "[SERVER] Player Kicked %s [Reason]: %s";
    public static final String INFO_PLAYER_JUST_CONNECTED = " just connected";
    public static final String INFO_CONNECTED_JOINED_WAITING_ROOM = "[SERVER] %s just connected and joined in Waiting Room";
    public static final String INFO_INSUFFICIENT_ADMIN_RIGHTS = "[INFO] You don't have admin rights to do this";
    public static final String QUESTION_SELECT_PLAYER_TO_KICK = "[SERVER] Select Player to Kick";
    public static final String INFO_ALL_PLAYERS_READY = "[INFO] Admin set all players Ready. Game Starting now";
    public static final String INFO_PLAYER_READY = "[INFO] You are set as Ready To Play";
    public static final String ERROR_FILE_IS_ODD = "The file your trying to read has an odd number of lines. Make it pair.";
    public static final String SERVER_SCORE_DASHBOARD = "* * * LIVE PLAYER SCORES * * * \n";
    public static final String SERVER_SCORE_DASHBOARD_PLAYER_NAME = "Players Name::  ";
    public static final String BREAK_LINE = "\n";
    public static final String SEND_MESSAGE_TO_PLAYER = "Write your message to player:\n";
    public static final String PLAYER_SENT_PM_TO_OTHER = "[INFO] %s: is having a private chat with %s.%s Who knows what ... %s";
    public static final String PLAYER_MESSAGE_FOR_PM = "[PM] %s: %s";
    public static final String PLAYER_MESSAGE_FOR_KICK = "[YOU WERE KICKED] Reason: %s";
    public static final String DEFINE_KICK_MESSAGE = "Write a kick reason:\n";
    public static final String INFO_PM_SENT = "PM sent.";
    public static final String YOU_KICKED_A_PLAYER = "Player kicked";
    public static final String INFO_LIST_PLAYERS = "User's List\n";
    public static final String INFO_SOMEONE_IS_WATCHING_LIST = "[INFO] %s is watching player's list";
    public static final String ERROR_NOT_ENOUGH_PLAYERS_IN_ROOM = "[INFO] No available players in room";
    public static final String SHOW_PLAYER_SCORES = "Your score: %d Your Lives: %d";
    public static final String SET_YOURSELF_READY = "Welcome to Game Room. Type /ready and lets GOOO!";

    public static final String ART_START_GAME =
            " __          __ ______  __      _____  ____   __  __  ______  _  _ \n" +
            " \\ \\        / /|  ____|| |     / ____|/ __ \\ |  \\/  ||  ____|| || |\n" +
            "  \\ \\  /\\  / / | |__   | |    | |    | |  | || \\  / || |__   | || |\n" +
            "   \\ \\/  \\/ /  |  __|  | |    | |    | |  | || |\\/| ||  __|  | || |\n" +
            "    \\  /\\  /   | |____ | |____  |____| |__| || |  | || |____ |_||_|\n" +
            "     \\/  \\/    |______||______|\\_____|\\____/ |_|  |_||______|(_)(_)\n\n" +
            "     Type /help to see some commands. Type '/' before the command\n\n";

    public static final String ART_GAME_RULES =
                    "   ______________________________\n" +
                    " / \\                             \\.\n" +
                    "|   |                            |.\n" +
                    " \\_ |      GAME RULES:           |.\n" +
                    "    |                            |.\n" +
                    "    |   1. Type the words in the |.\n" +
                    "    |      board as fast as      |.\n" +
                    "    |      possible              |.\n" +
                    "    |                            |.\n" +
                    "    |   2. More length more      |.\n" +
                    "    |      points                |.\n" +
                    "    |                            |.\n" +
                    "    |   3. If you miss the word  |.\n" +
                    "    |      three times, you lose |.\n" +
                    "    |                            |.\n" +
                    "    |      READY YOUR FINGERS!   |.\n" +
                    "    |   _________________________|___\n" +
                    "    |  /                            /.\n" +
                    "    \\_/____________________________/.";

    public static final String ART_GAME_TITLE =
                    "\n" +
                    "\n" +
                    " __          __           _        _____                      \n" +
                    " \\ \\        / /          | |      / ____|                     \n" +
                    "  \\ \\  /\\  / /__  _ __ __| |___  | |  __  __ _ _ __ ___   ___ \n" +
                    "   \\ \\/  \\/ / _ \\| '__/ _` / __| | | |_ |/ _` | '_ ` _ \\ / _ \\\n" +
                    "    \\  /\\  / (_) | | | (_| \\__ \\ | |__| | (_| | | | | | |  __/\n" +
                    "     \\/  \\/ \\___/|_|  \\__,_|___/  \\_____|\\__,_|_| |_| |_|\\___|\n" +
                    "                                                              \n" +
                    "                                                              \n" +
                    "\n";

    public static final String ART_CLEAR_SCREEN =
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    public static String drawWinner(String userName) {
        return
                "    -----------------\n" +
                        "    |@@@@|     |####|\n" +
                        "    |@@@@|     |####|\n" +
                        "    |@@@@|     |####|\n" +
                        "     |@@@|     |###|\n" +
                        "      |@@|     |##|\n" +
                        "      `@@|_____|##'\n" +
                        "           (O)\n" +
                        "        .-'''''-.\n" +
                        "      .'  * * *  `.\n" +
                        "     :  *       *  :\n" +
                        "          " + userName + "\n" +
                        "     :~           ~:\n" +
                        "     :  *       *  :\n" +
                        "      `.  * * *  .'\n" +
                        "        `-.....-'\n";
    }


    public static String getScoreMessage(int i) {
        return String.format("P.Score > %s | ", properties.getProperty(String.format("server.grid.score.%d", i)));
    }
}
