package org.academiadecodigo.wordsgame.entities.client;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.server.ServerDispatch;
import org.academiadecodigo.wordsgame.entities.users.Admin;
import org.academiadecodigo.wordsgame.entities.users.Player;
import org.academiadecodigo.wordsgame.entities.users.Roles;
import org.academiadecodigo.wordsgame.entities.users.User;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.ProjectProperties;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.game.stages.WaitingRoom;
import org.academiadecodigo.wordsgame.misc.Colors;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements Runnable{

    private Socket socket;
    private ProjectProperties properties;
    private Stage actualStage;
    private PrintStream printStream;
    private Prompt prompt;
    private PromptMenu<String> promptMenu;
    private int count = 0;

    public Client(@NotNull Socket socket, String filePath) {
        this.socket = socket;
        this.promptMenu = new PromptMenu<>();
        try {
            this.printStream = new PrintStream(socket.getOutputStream());
            this.prompt = new Prompt(socket.getInputStream(), printStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.actualStage = createInstanceOfStage(filePath);
        this.properties = ProjectProperties.getInstance();
    }

    /**
     * Run Thread
     */
    @Override
    public void run() {
        setupUser();
    }

    /**
     * Check the Name User choose to Login and creates user as per login.
     * @throws IOException
     */
    private void setupUser()  {

        if(count == 3) System.exit(0);
        String userName = promptMenu.createNewQuestion(Messages.INFO_SET_NICKNAME, prompt);

        if( isUserAdmin(userName) ) {
            setUserRole(Roles.ADMIN, userName);
        } else {
            if(userName.equals(properties.getProperty("admin.name"))) {
                setupUser();
            }
            setUserRole(Roles.PLAYER, userName);
        }
    }

    /**
     * Setup the user accordingly with the user role
     * @param role
     * @param userName
     */
    private void setUserRole(Roles role, String userName) {

        User user = null;
        if (role == Roles.ADMIN) user = setupNewAdminAccount();
        if (role == Roles.PLAYER) user = setupNewPlayerAccount(userName);

        registerInWaitingRoom(user);
        registerInChatManagerClass(user);

        if(actualStage.getUsersInTheRoom().size() == ServerDispatch.MAX_CLIENTS) createAndStartNewThread(actualStage);

        createAndStartNewThread(user);
        user.getClientDispatch().notifyPlayer(welcomeMessageNotifications(user), user);
    }


    /**
     * Check if userName typed corresponds to admin acc.
     * @param userName
     * @return boolean
     */
    private boolean isUserAdmin(@NotNull String userName) {
        return userName.equals(properties.getProperty("admin.name")) && adminLogin();
    }

    /**
     * Makes login with Admin account
     * If password is correct, proceed with Admin acc creation.
     * Else repeat all over again.
     * @return boolean
     */
    private boolean adminLogin(){
        failedTry();
        return promptMenu.createNewQuestion(Messages.INPUT_ADMIN_PASSWORD, prompt)
                .equals(properties.getProperty("admin.password"));
    }

    /**
     * Creates an Admin account
     * @return Admin Account
     */
    private @NotNull Admin setupNewAdminAccount() {
        resetTries();
        String userName = promptMenu.createNewQuestion(Messages.INFO_SET_NICKNAME, prompt);
        return new Admin(String.format("[ADMIN]%s", userName),0, 3, false, new ClientDispatch(socket, actualStage), socket, actualStage, false);
    }

    /**
     * Creates a Player Account
     * @param str
     * @return Player Account
     */
    private @NotNull Player setupNewPlayerAccount(String str) {
        return new Player(str, 0, 3, false, new ClientDispatch(socket, actualStage), socket, actualStage, false, false);
    }

    /**
     * After User created, send messages to Server for Welcome
     * @param user
     * @return
     */
    private String welcomeMessageNotifications(User user) {
        ChatCommandsMessagesTrafficManager.sendMessageToChat(user, String.format(Messages.INFO_CONNECTED_JOINED_WAITING_ROOM, user.getUserName()));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(Colors.WHITE_UNDERLINED + user.getUserName() + Colors.RESET + Messages.INFO_PLAYER_JUST_CONNECTED);
        return (Messages.ART_START_GAME);
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
     * Singleton of Stage Waiting Room
     * Starts the thread after users are created
     */
    private Stage createInstanceOfStage(String filePath) {
        return WaitingRoom.getInstance(new Grid(filePath), ServerDispatch.MAX_CLIENTS, new CopyOnWriteArrayList<>());
    }

    /**
     * If User fails to login it has 3 times.
     */
    private void failedTry(){
        count ++;
    }

    /**
     * Reset login tries to 0 after succedeed login
     */
    private void resetTries() {
        count = 0;
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
