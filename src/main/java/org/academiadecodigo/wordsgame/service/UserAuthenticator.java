package org.academiadecodigo.wordsgame.service;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.entities.users.Role;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticator {

    static Database db;

    static {
        try {
            db = Database.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean authenticateRoot(String user, String pass) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement pstmt;
        try {
            db = Database.getInstance();
            pstmt = db.getConnection().prepareStatement(query);

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                return role.equals("ROOT");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void register(Role role, String userName, String password) {
        String query = "INSERT INTO users (username, password, role) VALUES ('"+ userName + "', '"+ password + "', '"+ role +"')";
        db.executeUpdate(query);
    }

    public static boolean login(String userName, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement statement;
        try {
            statement = db.getConnection().prepareStatement(query);

            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            // if true exists, else doesn't.
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Role getUserRole(String userName) {
        String query = "SELECT role FROM users WHERE username = ?";
        PreparedStatement statement;
        try {
            statement = db.getConnection().prepareStatement(query);

            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User exists in the database
                String roleStr = resultSet.getString("role");
                return Role.valueOf(roleStr.toUpperCase());
            } else {
                // User does not exist in the database
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
