package org.academiadecodigo.wordsgame.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Messages {
    private static final Properties messages;
    private static final Properties property;

    static {
        messages = new Properties();
        property = new Properties();
        try (InputStream input = Messages.class.getResourceAsStream("/messages.properties")) {
            messages.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream input = Messages.class.getResourceAsStream("/application.properties")) {
            property.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMessage(String key) {
        return messages.getProperty(key);
    }

    public static String getProperty(String key) {
        return property.getProperty(key);
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
        return String.format("P.Score > %s | ", getMessage(String.format("server.grid.score.%d", i)));
    }
}

