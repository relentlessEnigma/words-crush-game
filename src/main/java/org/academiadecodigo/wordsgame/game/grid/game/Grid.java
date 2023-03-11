package org.academiadecodigo.wordsgame.game.grid.game;

import org.academiadecodigo.wordsgame.misc.Messages;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Grid {

    private String PATH;
    private final int cols;
    private static String[][] wordMatrix;
    private int rows;

    public Grid(String filePath){
        this.PATH = filePath;
        this.cols = 10;
        this.rows = checkRowSize();
        wordMatrix = new String[this.rows][this.cols];
    }

    /**
     * Check How many Rows will be drawed based in amount of words in file
     * The amount of words in file must be even
     *
     * @return
     */
    private int checkRowSize() {
        int linesInFile;
        try (BufferedReader in = new BufferedReader(new FileReader(PATH))) {
            linesInFile = (int) in.lines().count();
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getMessage());
            return BackupGrid.setWordsForMatrixBackup().length;
        }

        if (linesInFile % 2 == 0) {
            return linesInFile / 10;
        } else {
            System.out.println(Messages.get("ERROR_FILE_IS_ODD"));
        }
        return linesInFile;
    }

    /**
     * Set Bi-Dimensional Array Keys and Values
     */
    public void setWordsForMatrix() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(PATH));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String word = in.readLine();
                    int countSpaces = 10 - word.length();
                    for (int k = 0; k < countSpaces; k++) {
                        word += " ";
                    }
                    wordMatrix[i][j] = word;
                }
            }
        } catch (IOException e) {
            wordMatrix = BackupGrid.setWordsForMatrixBackup();
        }
    }

    /**
     * Draw Matrix
     * @return
     */
    public String drawMatrix() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < wordMatrix.length; i++) {

            for (int j = 0; j < wordMatrix[i].length; j++) {

                sb.append(wordMatrix[i][j]);

            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Check Player Input and Compare it with the List
     * @param str
     * @return
     */
    public synchronized int checkPlayerInput(String str) {

        int score = 0;

        for (int i = 0; i < wordMatrix.length; i++) {
            for (int j = 0; j < wordMatrix[i].length; j++) {

                //Remove the blank spaces of the word:
                String trimmedWord = wordMatrix[i][j].trim();

                //If word equals to player input:
                if (str.equals(trimmedWord)) {

                    score += trimmedWord.length();
                    wordMatrix[i][j] = "          ";
                }
            }
        }
        return score;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public static String[][] getWordMatrix() {
        return wordMatrix;
    }
}
