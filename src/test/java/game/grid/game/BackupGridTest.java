package game.grid.game;

import org.academiadecodigo.wordsgame.game.grid.game.BackupGrid;
import org.junit.Test;
import static org.junit.Assert.*;

public class BackupGridTest {

    @Test
    public void testSetWordsForMatrixBackup2() {
        String[][] expected = {
                {"teste     ", "ola       ", "witness   ", "acid      ", "location  ", "wake      ", "X-ray     ", "export    ", "indirect  ", "peanut    "},
                {"owl       ", "campaign  ", "bulb      ", "quantity  ", "introduce ", "coalition ", "sink      ", "magazine  ", "feature   ", "slice     "},
                {"industry  ", "purpose   ", "knife     ", "immune    ", "product   ", "sum       ", "unrest    ", "promise   ", "allow     ", "gesture   "},
                {"exploit   ", "delete    ", "bark      ", "reactor   ", "electron  ", "missile   ", "damn      ", "witness   ", "acid      ", "location  "},
                {"wake      ", "X-ray     ", "export    ", "indirect  ", "peanut    ", "owl       ", "campaign  ", "bulb      ", "quantity  ", "introduce "},
                {"coalition ", "sink      ", "magazine  ", "feature   ", "slice     ", "industry  ", "purpose   ", "reactor   ", "electron  ", "seminar   "},
                {"grace     ", "hand      ", "episode   ", "laborer   ", "ideal     ", "equinox   ", "affinity  ", "herb      ", "oil       ", "winter    "},
                {"embryo    ", "vision    ", "incident  ", "pound     ", "excess    ", "          ", "          ", "          ", "          ", "          "}
        };
        String[][] actual = BackupGrid.setWordsForMatrixBackup();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testSetWordsForMatrixBackup() {
        String[][] words = BackupGrid.setWordsForMatrixBackup();
        System.out.println(words);
        assertNotNull(words);
        assertEquals(8, words.length);
        assertEquals(10, words[0].length);
        assertEquals("teste     ", words[0][0]);
        assertEquals("winter    ", words[6][9]);
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length; j++) {
                String word = words[i][j];
                assertEquals(10, word.length());
            }
        }
    }

}
