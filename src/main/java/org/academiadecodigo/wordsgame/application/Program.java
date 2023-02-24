package org.academiadecodigo.wordsgame.application;

import org.academiadecodigo.wordsgame.entities.server.GameServer;

import java.io.IOException;

public class Program {

    public static void main(String[] args) {

        GameServer server = new GameServer(8001, 2, "src/main/resources/teste.txt");

        try {
            server.manageNewConnections();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
