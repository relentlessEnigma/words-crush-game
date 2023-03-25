package org.academiadecodigo.wordsgame.entities.client;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.server.GameServer;
import org.academiadecodigo.wordsgame.entities.users.*;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.ProjectProperties;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.game.stages.WaitingRoom;
import org.academiadecodigo.wordsgame.misc.Colors;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class ClientDispatch implements Runnable {

    private ProjectProperties properties;
    private Stage actualStage;
    private Prompt prompt;
    private PromptMenu<Integer> promptMenu;
    private int count = 0;
    private UserManager um;

    private ChatCommandsMessagesTrafficManager chat;
    private Socket socket;
    private PrintWriter outStream;

    public ClientDispatch(Socket socket, String filePath) {
        this.socket = socket;
        this.properties = ProjectProperties.getInstance();

        this.promptMenu = new PromptMenu<>();
        try {
            this.prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
            this.outStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.actualStage = createInstanceOfStage(filePath);
        this.chat = new ChatCommandsMessagesTrafficManager();
    }

    @Override
    public void run() {
        setupUser();
    }

    /**
     * Check the Name User choose to Login and creates user as per login.
     * @throws IOException
     */
    private void setupUser()  {

        this.um = new UserManager(this.outStream, this.prompt);

        int connectionType = promptMenu.createNewMenu(new String[]{"Register", "Login"}, "Select", prompt);
        if(connectionType == 1) {
            um.register();
        }
        um.login();
        setUserRole(um.getUserRole(), um.getUserName());
    }

    /**
     * Setup the user accordingly with the user role
     * @param role
     * @param userName
     */
    private void setUserRole(Roles role, String userName) {
        User user = null;
        if (role == Roles.ADMIN || role == Roles.ROOT) user = setupNewAdminAccount();
        if (role == Roles.PLAYER) user = setupNewPlayerAccount(userName);

        registerInWaitingRoom(user);
        registerInChatManagerClass(user);

        if(actualStage.getUsersInTheRoom().size() == GameServer.MAX_CLIENTS) createAndStartNewThread(actualStage);

        createAndStartNewThread(user);
        notifyPlayer(welcomeMessageNotifications(user));
    }

    /**
     * Creates a Player Account
     * @param str
     * @return Player Account
     */
    private @NotNull Player setupNewPlayerAccount(String str) {
        return new Player(str, 0, 3, false, this, socket, actualStage, false, false);
    }

    /**
     * Creates an Admin account
     * @return Admin Account
     */
    private @NotNull Admin setupNewAdminAccount() {
        // TODO: in future get this data from DTO
        return new Admin(String.format("[ADMIN]%s", this.um.getUserName()),0, 3, false, this, socket, actualStage, false);
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
     * Singleton of Stage Waiting Room
     * Starts the thread after users are created
     */
    private Stage createInstanceOfStage(String filePath) {
        return WaitingRoom.getInstance(new Grid(filePath), GameServer.MAX_CLIENTS, new CopyOnWriteArrayList<>());
    }

    /**
     * Create and Start a New Thread
     * @param obj
     */
    private void createAndStartNewThread(Object obj) {
        Thread t = new Thread((Runnable) obj);
        t.start();
    }

    /**
     * Sends the rules of the game to all users
     * through User's personal CP (Client Dispatch)
     */
    public void sendRules() {

        this.outStream.println(Messages.get("ART_GAME_RULES"));
        this.outStream.println(Messages.get("INFO_STARTING_GAME_IN"));

        for (int i = 10; i >= 0; i--) {
            try {
                Thread.sleep(0); //TODO: correct this to 1000 mss again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i != 0) {
                this.outStream.println(i + " ");
            }
        }
    }

    /**
     * Generates personalized message to self player.
     * @param message to be sent.
     */
    public void notifyPlayer(String message) {
        this.outStream.println(message);
    }
}
