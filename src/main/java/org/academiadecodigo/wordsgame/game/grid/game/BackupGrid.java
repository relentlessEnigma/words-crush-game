package org.academiadecodigo.wordsgame.game.grid.game;

/**
 * This class is for Backup Purpose only
 * If the outside file is corrupted or something
 * wrong with it, the game will automatically
 * start with the class words
 */
public class BackupGrid {

    private static String[] wordsList = {
            "caralho", "damn", "witness",
            "acid", "location", "wake",
            "X-ray", "export", "indirect",
            "peanut", "owl", "campaign", "bulb",
            "quantity", "introduce", "coalition",
            "sink", "magazine", "feature", "slice",
            "industry", "purpose", "knife", "immune",
            "product", "sum", "unrest", "promise", "allow",
            "gesture", "exploit", "delete", "bark", "reactor",
            "electron", "missile", "damn", "witness",
            "acid", "location", "wake",
            "X-ray", "export", "indirect",
            "peanut", "owl", "campaign", "bulb",
            "quantity", "introduce", "coalition",
            "sink", "magazine", "feature", "slice",
            "industry", "purpose", "reactor", "electron",
            "seminar", "grace", "hand", "episode", "laborer",
            "ideal", "equinox", "affinity", "herb", "oil",
            "winter", "embryo", "vision", "incident", "pound"
    };

    /**
     * Returns words to grid
     * @param wordMatrix
     * @return Vector 2D String[][]
     */
    public static String[][] setWordsForMatrixBackup(String[][] wordMatrix) {

        for(int i = 0; i <= 4; i ++) {
            for(int j = 0; j <= 9; j++) {
                wordMatrix[i][j] = wordsList[(i*10)+j];
            }
        }
        return wordMatrix;
    }
}
