package org.academiadecodigo.wordsgame.service;

import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserService {
    private Database database;

    public UserService() {
        try {
            this.database = Database.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = " + id + ";";
        ResultSet resultSet = database.executeQuery(query);
        String userName = resultSet.getString(1);
        String password = resultSet.getString(2);

        return Map.of(userName, password);
    }

    public void createNewUser() {
        //CReate clientDispatch

        //Create User and send clientDispatch inside it
    }

    public void getUsers() {
        // implementation to retrieve list of users from database
    }

    public void saveUser(User user) {
        // implementation to save user to database
    }

    public void deleteUser(int id) {
        // implementation to delete user from database
    }

    public void saveScore(int score) {
        // TODO missing implementation
    }

    public int getScore() {
        // TODO missing implementation
        return 0;
    }
}
