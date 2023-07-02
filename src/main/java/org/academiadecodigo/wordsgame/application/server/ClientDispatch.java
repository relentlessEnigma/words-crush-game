package org.academiadecodigo.wordsgame.application.server;

import lombok.Getter;
import lombok.Setter;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.wordsgame.entities.users.*;
import org.academiadecodigo.wordsgame.game.ChatCommandsMessagesTrafficManager;
import org.academiadecodigo.wordsgame.game.PromptMenu;
import org.academiadecodigo.wordsgame.game.grid.game.Grid;
import org.academiadecodigo.wordsgame.game.stages.Stage;
import org.academiadecodigo.wordsgame.game.stages.WaitingRoom;
import org.academiadecodigo.wordsgame.misc.Colors;
import org.academiadecodigo.wordsgame.misc.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class ClientDispatch implements Runnable {

    private int id;
    private Stage actualStage;
    private Prompt prompt;
    private PromptMenu<Integer> promptMenu;
    private int count = 0;
    private UserManager um;
    private Boolean isPlayerNotReading = false;
    private LinkedList<String> bufferedMessages;

    private ChatCommandsMessagesTrafficManager chat;
    private Socket socket;
    private PrintWriter outStream;

    public ClientDispatch(Socket socket, String filePath) {
        this.socket = socket;
        this.promptMenu = new PromptMenu<>();
        
        try {
            this.prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
            this.outStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.actualStage = createInstanceOfStage(filePath);
        this.chat = new ChatCommandsMessagesTrafficManager();
        this.bufferedMessages = new LinkedList<>();
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
    private void setUserRole(Role role, String userName) {
        User user = null;
        if (role == Role.ADMIN || role == Role.ROOT) user = setupNewAdminAccount();
        if (role == Role.PLAYER) user = setupNewPlayerAccount(userName);

        registerInWaitingRoom(user);
        registerInChatManagerClass(user);

        if(actualStage.getUsersInTheRoom().size() == GameServer.MAX_CLIENTS) createAndStartNewThread(actualStage);

        createAndStartNewThread(user);
        welcomeMessageNotifications(user);
    }

    /**
     * Creates a Player Account
     * @param str
     * @return Player Account
     */
    private @NotNull Player setupNewPlayerAccount(String str) {
        return new Player(this.id, str, 0, 3, false, this, socket, actualStage, false, false);
    }

    /**
     * Creates an Admin account
     * @return Admin Account
     */
    private @NotNull Admin setupNewAdminAccount() {
        // TODO: in future get this data from DTO
        return new Admin(this.id, String.format("[ADMIN]%s", this.um.getUserName()),0, 3, false, this, socket, actualStage, false);
    }

    /**
     * After User created, send messages to Server for Welcome
     * @param user
     * @return
     */
    private void welcomeMessageNotifications(User user) {
        ChatCommandsMessagesTrafficManager.sendMessageToChat(user, String.format(Messages.getMessage("INFO_CONNECTED_JOINED_WAITING_ROOM"), user.getUserName()));
        ChatCommandsMessagesTrafficManager.sendMessageToServer(
                String.format(Messages.getMessage("INFO_PLAYER_JUST_CONNECTED"), Colors.WHITE_UNDERLINED, user.getUserName(), Colors.RESET)
        );
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

        this.outStream.println(Messages.getMessage("ART_GAME_RULES"));
        this.outStream.println(Messages.getMessage("INFO_STARTING_GAME_IN"));

        for (int i = 10; i >= 0; i--) {
            try {
                Thread.sleep(1000);
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

        // saves the message to the list
        if(isPlayerNotReading) {
            this.bufferedMessages.add(message);
            return;
        }

        // send the last message
        this.outStream.println(message);
    }

    private void sendBufferedMessagesToPlayer() {
        Optional.ofNullable(bufferedMessages).ifPresent(msg -> msg.forEach(this.outStream::println));
        assert this.bufferedMessages != null;
        this.bufferedMessages.clear();
    }

    public void setIsPlayerNotReading(Boolean state) {
        this.isPlayerNotReading = state;
        if(!state) this.sendBufferedMessagesToPlayer();
    }
}
