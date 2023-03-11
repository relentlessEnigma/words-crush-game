package org.academiadecodigo.wordsgame.game.grid.game;

/**
 * This class is for Backup Purpose only
 * If the outside file is corrupted or something
 * wrong with it, the game will automatically
 * start with the class words
 */
public class BackupGrid {

    //Length must allways be of 50
    private static String[] wordsList = {
            "teste", "ola", "witness", "acid", "location",
            "wake", "X-ray", "export", "indirect", "peanut",
            "owl", "campaign", "bulb", "quantity", "introduce",
            "coalition", "sink", "magazine", "feature", "slice",
            "industry", "purpose", "knife", "immune", "product",
            "sum", "unrest", "promise", "allow", "gesture",
            "exploit", "delete", "bark", "reactor", "electron",
            "missile", "damn", "witness", "acid", "location",
            "wake", "X-ray", "export", "indirect", "peanut",
            "owl", "campaign", "bulb", "quantity", "introduce",
            "coalition", "sink", "magazine", "feature", "slice",
            "industry", "purpose", "reactor", "electron", "seminar",
            "grace", "hand", "episode", "laborer", "ideal",
            "equinox", "affinity", "herb", "oil", "winter",
            "embryo", "vision", "incident", "pound", "excess"
    };

    private static int cols = 10;
    private static int rows = (int) Math.ceil((double) wordsList.length / cols);

    /**
     * Returns words to grid
     * @return Vector 2D String[][]
     */
    public static String[][] setWordsForMatrixBackup() {
        String[][] newWordsList = new String[rows][cols];
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if (counter < wordsList.length) {
                    newWordsList[i][j] = addSpacesToWord(wordsList[counter]);
                } else {
                    newWordsList[i][j] = addSpacesToWord("");
                }
                counter ++;
            }
        }
        return newWordsList;
    }


    private static String addSpacesToWord(String word) {
        StringBuilder x = new StringBuilder();
        for (int j = 0; j < 10; j++) {
            if (j < word.length()) {
                x.append(word.charAt(j));
            } else {
                x.append(" ");
            }
        }
        return x.toString();
    }
}
