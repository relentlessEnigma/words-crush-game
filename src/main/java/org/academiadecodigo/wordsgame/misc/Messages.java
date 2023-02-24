package org.academiadecodigo.wordsgame.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Messages {
    private static final Properties messages;

    static {
        messages = new Properties();
        try (InputStream input = Messages.class.getResourceAsStream("/messages.properties")) {
            messages.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return messages.getProperty(key);
    }

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
        return String.format("P.Score > %s | ", get(String.format("server.grid.score.%d", i)));
    }
}

