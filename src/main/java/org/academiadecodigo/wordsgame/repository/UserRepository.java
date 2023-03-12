package org.academiadecodigo.wordsgame.repository;

import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserRepository {
    private Database database;

    public UserRepository() throws SQLException {
        this.database = Database.getInstance();
    }

    public Map<String, String> getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = " + id + ";";
        ResultSet resultSet = database.executeQuery(query);
        String userName = resultSet.getString(1);
        String password = resultSet.getString(2);

        return Map.of(userName, password);
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
}
