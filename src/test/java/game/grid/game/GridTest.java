package game.grid.game;

import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    private final String TEST_FILE_PATH = "src/test/resources/test_words.txt";
    private final String INVALID_FILE_PATH = "nonexistent.txt";
    private final int EXPECTED_ROWS = 2;
    private Grid grid;

    @BeforeEach
    public void setUp() {
        grid = new Grid(TEST_FILE_PATH);
    }

    @Test
    public void testCheckRowSizeWithValidFile() {
        grid = new Grid(TEST_FILE_PATH);
        assertEquals(EXPECTED_ROWS, grid.getRows());
    }

    @Test
    public void testCheckRowSizeWithInvalidFileShouldReturnWordsFromBackup() {
        grid = new Grid(INVALID_FILE_PATH);
        assertEquals(8, grid.getRows());
    }

    @Test
    public void testSetWordsForMatrix() {
        grid.setRows(EXPECTED_ROWS);
        grid.setWordsForMatrix();
        String[][] expectedMatrix = {
                {"abacus    ", "barbecue  ", "chocolate ", "dolphin   ", "elephant  ", "festival  ", "gorilla   ", "harmony   ", "indigo    ", "jovial    "},
                {"kangaroo  ", "lavender  ", "mountain  ", "nectar    ", "octopus   ", "panther   ", "quality   ", "rainbow   ", "saffron   ", "tulip     "}
        };
        Assertions.assertArrayEquals(expectedMatrix, Grid.getWordMatrix());
    }

    @Test
    public void testDrawMatrix() {
        //given
        grid.setRows(EXPECTED_ROWS);
        grid.setWordsForMatrix();

        // when
        String result = grid.drawMatrix();

        //then
        String expectedOutput = "abacus    barbecue  chocolate dolphin   elephant  festival  gorilla   harmony   indigo    jovial    \n" +
                "kangaroo  lavender  mountain  nectar    octopus   panther   quality   rainbow   saffron   tulip     \n";

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testCheckPlayerInputWithValidInput() throws IOException {
        // given
        grid.setRows(EXPECTED_ROWS);
        grid.setWordsForMatrix();
        String wordTest = "indigo";
        int expectedScore = wordTest.length();

        // when
        int actualScore = grid.checkPlayerInput(wordTest);

        // then
        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void testCheckPlayerInputForAlreadyFoundWord() {
        grid.setWordsForMatrix();

        // Test correct input
        int expectedScore = 6;
        assertEquals(expectedScore, grid.checkPlayerInput("abacus"));

        // Test input already found
        expectedScore = 0;
        assertEquals(expectedScore, grid.checkPlayerInput("abacus"));
    }

    @Test
    public void testCheckPlayerInputWithInvalidInput() {
        //given
        grid.setRows(EXPECTED_ROWS);
        grid.setWordsForMatrix();
        int expectedScore = 0;

        //when
        int actualScore = grid.checkPlayerInput("invalid");

        //then
        assertEquals(expectedScore, actualScore);
    }
}
