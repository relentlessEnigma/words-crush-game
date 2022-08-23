package org.academiadecodigo.wordsgame.game.grid.server;

import lombok.Getter;

import java.util.List;

@Getter
public enum ScoresLevelType  {

    LEVEL0(0),
    LEVEL1(15),
    LEVEL2(50),
    LEVEL3(100),
    LEVEL4(150),
    LEVEL5(200),
    LEVEL6(300);

    private Integer score;

    ScoresLevelType(Integer score) {
        this.score = score;
    }

    public static List<ScoresLevelType> getEnumAsStream() {
        return List.of(ScoresLevelType.values());
    }

    public static Integer returnEnumValueByValueCheck(Integer value){
        int counter = 0;

        for(ScoresLevelType v: getEnumAsStream()) {

            if(v.score.equals(value)) {
                return counter;
            }
            counter++;
        }
        return counter;
    }

}
