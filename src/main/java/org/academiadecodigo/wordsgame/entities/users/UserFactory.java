package org.academiadecodigo.wordsgame.entities.users;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.client.ClientDispatch;
import org.academiadecodigo.wordsgame.entities.server.GameServer;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.ProjectProperties;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.game.stages.WaitingRoom;
import org.academiadecodigo.wordsgame.misc.Colors;
import org.academiadecodigo.wordsgame.database.Database;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * NEEDS WORK
 */
public class UserFactory {

    private ProjectProperties properties;
    private int count = 0;
    private PromptMenu<String> promptMenu = new PromptMenu();
    private Prompt prompt;
    private Stage actualStage;
    private ClientDispatch clientDispatch;
    private Socket socket;

    public UserFactory(Prompt prompt, String filePath, @NotNull ClientDispatch clientDispatch){
        this.prompt = prompt;
        this.properties = ProjectProperties.getInstance();
        this.actualStage = createInstanceOfStage(filePath);
        this.clientDispatch = clientDispatch;
        this.socket = clientDispatch.getSocket();

        setupUser();
    }

    /**
     * Singleton of Stage Waiting Room
     * Starts the thread after users are created
     */
    private Stage createInstanceOfStage(String filePath) {
        return WaitingRoom.getInstance(new Grid(filePath), GameServer.MAX_CLIENTS, new CopyOnWriteArrayList<>());
    }

    /**
     * Check the Name User choose to Login and creates user as per login.
     * @throws IOException
     */
    private User setupUser()  {

        if(count == 3) System.exit(0);

        String userName = promptMenu.createNewQuestion(Messages.get("INFO_SET_NICKNAME"), prompt);
        String password = promptMenu.createNewQuestion(Messages.get("INFO_SET_PASSWORD"), prompt);

        if( isUserAdmin(userName, password) ) {
            setUserRole(Role.ADMIN, userName);
        }

        if(userName.equals(properties.getProperty("admin.name"))) {
            setupUser();
        }

        return setUserRole(Role.PLAYER, userName);
    }

    /**
     * Setup the user accordingly with the user role
     * @param role
     * @param userName
     */
    private User setUserRole(Role role, String userName) {

        User user = null;
        if (role == Role.ADMIN) user = setupNewAdminAccount();
        if (role == Role.PLAYER) user = setupNewPlayerAccount(userName);

        registerInWaitingRoom(user);
        registerInChatManagerClass(user);

        if(actualStage.getUsersInTheRoom().size() == GameServer.MAX_CLIENTS) createAndStartNewThread(actualStage);

        createAndStartNewThread(user);
        clientDispatch.notifyPlayer(welcomeMessageNotifications(user));
        return user;
    }


    /**
     * Check if userName typed corresponds to admin acc.
     * @param userName
     * @return boolean
     */
    private boolean isUserAdmin(String userName, String password) {
        Database db = null;
        try {
            // get the connection to the database
            db = Database.getInstance();
            db.startDb();

            // prepare the SQL statement to retrieve user info by username and password
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = db.getConnection().prepareStatement(query);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);

            // execute the query and retrieve the results
            ResultSet rs = pstmt.executeQuery();

            // check if the query returned a result
            if (rs.next()) {
                // retrieve the user's role from the result set
                String role = rs.getString("role");
                return role.equals("ADMIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Makes login with Admin account
     * If password is correct, proceed with Admin acc creation.
     * Else repeat all over again.
     * @return boolean
     */
    private boolean adminLogin(){
        failedTry();
        return promptMenu.createNewQuestion(Messages.get("INPUT_ADMIN_PASSWORD"), prompt)
                .equals(properties.getProperty("admin.password"));
    }

    /**
     * Creates an Admin account
     * @return Admin Account
     */
    private @NotNull Admin setupNewAdminAccount() {
        resetTries();
        String userName = promptMenu.createNewQuestion(Messages.get("INFO_SET_NICKNAME"), prompt);
        return new Admin(String.format("[ADMIN]%s", userName),0, 3, false, this.clientDispatch, socket, actualStage, false);
    }

    /**
     * Creates a Player Account
     * @param str
     * @return Player Account
     */
    private @NotNull Player setupNewPlayerAccount(String str) {
        return new Player(str, 0, 3, false, this.clientDispatch, socket, actualStage, false, false);
    }

    /**
     * Reset login tries to 0 after succedeed login
     */
    private void resetTries() {
        count = 0;
    }

    /**
     * After User created, send messages to Server for Welcome
     * @param user
     * @return
     */
    private String welcomeMessageNotifications(User user) {
        ChatCommandsMessagesTrafficManager.sendMessageToChat(user, String.format(Messages.get("INFO_CONNECTED_JOINED_WAITING_ROOM"), user.getUserName()));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(Colors.WHITE_UNDERLINED + user.getUserName() + Colors.RESET + Messages.get("INFO_PLAYER_JUST_CONNECTED"));
        return (Messages.get("ART_START_GAME"));
    }

    /**
     * Send created user to waiting room
     * @param user
     */
    private void registerInWaitingRoom(User user){
        if(actualStage instanceof WaitingRoom) {
            ((WaitingRoom) actualStage).registerUserInStage(user);
        }
    }

    /**
     * Send created user to Chat Class manager
     * @param user
     */
    private void registerInChatManagerClass(User user){
        ChatCommandsMessagesTrafficManager.registerUserForChatsManagement(user);
    }

    /**
     * If User fails to login it has 3 times.
     */
    private void failedTry(){
        count ++;
    }

    /**
     * Create and Start a New Thread
     * @param obj
     */
    private void createAndStartNewThread(Object obj) {
        Thread t = new Thread((Runnable) obj);
        t.start();
    }
}
