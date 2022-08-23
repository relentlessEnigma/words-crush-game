package org.academiadecodigo.wordsgame.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

public class PromptMenu <T> {

    public T createNewMenu(String[] strArray, String setMenuMessage, Prompt prompt) {

        MenuInputScanner scanner = new MenuInputScanner(strArray);

        scanner.setMessage(setMenuMessage);

        return (T) prompt.getUserInput(scanner);
    }

    public T createNewQuestion(String questTitle, Prompt prompt) {

        StringInputScanner question = new StringInputScanner();

        question.setMessage(questTitle);

        return (T) prompt.getUserInput(question);
    }
}
