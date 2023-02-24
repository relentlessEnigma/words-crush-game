package org.academiadecodigo.wordsgame.game.grid.server;

import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ProjectProperties;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.util.*;

public class ServerGrid {

    private final static int ROWS = (Integer.parseInt(ProjectProperties.getInstance().getProperty("server.grid.rows.number")));
    private ScoresService sc;

    public ServerGrid() {
        this.sc = new ScoresService();
    }

    /**
     * Create Grid
     * @param usersQuantity
     * @return String[][]
     */
    private String[][] createGridStructure(int usersQuantity){

        String grid[][] = new String[ROWS][usersQuantity];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < usersQuantity; j++) {
                grid[i][j] = " ";
            }
        }
        return grid;
    }

    /**
     * Draw Grid with full details
     * @param userList
     */
    public String drawGrid(List<User> userList) {

        StringBuilder sb = new StringBuilder();
        String[][] grid = createGridStructure(userList.size());
        sendPlayerScoresToGrid(userList, grid);

        sb.append(Messages.get("SERVER_SCORE_DASHBOARD") + Messages.get("BREAK_LINE"));
        sb.append(Messages.get("SERVER_SCORE_DASHBOARD_PLAYER_NAME") + Messages.get("BREAK_LINE"));
        for (User user : userList) {
            sb.append(user.getUserName().charAt(0)).append(user.getUserName().charAt(1)).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < ROWS; i++) {
            sc.getScoreText(i);
            for (int j = 0; j < userList.size(); j++) {
                sb.append("[" + grid[i][j] + "]");
            }
            sb.append(" |");
        }
        return sb.toString();
    }

    private void sendPlayerScoresToGrid(List<User> userList, String[][] grid){

        int index;
        for (int i = 0; i < ScoresLevelType.values().length; i++) {
            for(int j = 0; j < userList.size(); j++) {
                index = setIndexBasedOnScore(sc.getNearestValue(userList.get(j).getScore(), sc.getScoresFromProperties()));
                grid[i][index] = String.valueOf(userList.get(j).getLives());
            }
        }
    }

    public int setIndexBasedOnScore(Integer score) {

        return ScoresLevelType.returnEnumValueByValueCheck(score);
    }
}