package org.academiadecodigo.wordsgame.application;

import org.academiadecodigo.wordsgame.application.server.GameServer;
import java.io.IOException;
import java.sql.SQLException;

public class Program {

    public static void main(String[] args) {

        GameServer server;
        try {
            server = new GameServer(8001, 2, "src/main/resources/teste.txt");
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Check if Your MySQL service is running!");
            throw new RuntimeException(e);
        }

        server.manageNewConnections();
    }
}
