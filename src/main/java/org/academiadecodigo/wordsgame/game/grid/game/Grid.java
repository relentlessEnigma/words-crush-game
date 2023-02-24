package org.academiadecodigo.wordsgame.game.grid.game;

import org.academiadecodigo.wordsgame.misc.Messages;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
        checkRowSize();
        wordMatrix = new String[this.rows][this.cols];
    }

    /**
     * Check How many Rows will be drawed based in amount of words in file
     * The amount of words in file must be even
     */
    private void checkRowSize() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(PATH));
        } catch (FileNotFoundException e) {
            BackupGrid.setWordsForMatrixBackup(wordMatrix);
        }
        int linesInFile;
        if((linesInFile = (int) in.lines().count()) % 2 == 0) {
            this.rows = linesInFile / 10;
        } else {
            System.out.println(Messages.get("ERROR_FILE_IS_ODD"));
        }
    }

    /**
     * Set Bi-Dimensional Array Keys and Values
     */
    public void setWordsForMatrix() {

        try {
            BufferedReader in = new BufferedReader(new FileReader(PATH));
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String word = in.readLine();
                    int countSpaces = 10 - word.length();
                    for (int k = 0; k < countSpaces; k++) {
                        word += String.valueOf(sb.append(" "));
                        sb.delete(0, sb.length());
                    }
                    wordMatrix[i][j] = word;
                }
            }
        } catch (FileNotFoundException e) {
            BackupGrid.setWordsForMatrixBackup(wordMatrix);
        } catch (IOException e) {
            e.printStackTrace();
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
}
