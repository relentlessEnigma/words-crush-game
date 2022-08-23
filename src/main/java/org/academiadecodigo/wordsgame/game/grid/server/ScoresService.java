package org.academiadecodigo.wordsgame.game.grid.server;

import org.academiadecodigo.wordsgame.game.ProjectProperties;
import org.academiadecodigo.wordsgame.misc.Messages;
import java.util.HashMap;
import java.util.Map;

public class ScoresService {

    private ProjectProperties properties;

    public ScoresService() {
        this.properties = ProjectProperties.getInstance();
    }

    /**
     * Get Scores from project properties
     * @return Integer[]
     */
    public Integer[] getScoresFromProperties(){
        Integer[] scores = new Integer[6];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = Integer.parseInt(properties.getProperty("server.grid.score."+i));
        }
        return scores;
    }

    /**
     * Get the nearest value of a given value from a given matrix
     * @param score
     * @param y
     * @return int
     */
    public int getNearestValue(int score, Integer[] y) {
        Integer count = 0;
        for(Integer x : y) {
            if(score - x < 0) return count;
            count = x;
        }
        return count;
    }

    /**
     * Returns Text related with the score to the Grid
     * @param option
     * @return
     */
    public String getScoreText(int option){

        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            map.put(i, Messages.getScoreMessage(i));
        }
        return map.get(option);
    }

}
