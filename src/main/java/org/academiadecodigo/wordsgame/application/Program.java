package org.academiadecodigo.wordsgame.application;

import org.academiadecodigo.wordsgame.entities.server.ServerDispatch;

import java.io.IOException;

public class Program {

    public static void main(String[] args) {

        ServerDispatch server = new ServerDispatch(8001, 2, "src/main/resources/teste.txt");

        try {
            server.manageNewConnections();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
