package org.academiadecodigo.wordsgame.game.commands.executors.list;

import lombok.Getter;

@Getter
public enum CommandsList {

    PM("/pm"),
    KICK("/kick"),
    STARTALL("/start -a"),
    HELP("/help"),
    LIST("/list"),
    START("/start"),
    READY("/ready");

    private String command;

    CommandsList(String command) {
        this.command = command;
    }
}
