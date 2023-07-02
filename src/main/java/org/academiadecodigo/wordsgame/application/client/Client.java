package org.academiadecodigo.wordsgame.application.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
    private JTextField messageField;
    private JButton sendButton;
    private JTextArea chatArea;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverAddress, int serverPort) throws IOException {
        super("Chat Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        messageField = new JTextField();
        sendButton = new JButton("Send");
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        // Add components to frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        JPanel messagePanel = new JPanel(new FlowLayout());
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        panel.add(messagePanel, BorderLayout.SOUTH);
        add(panel);
        panel.setPreferredSize(new Dimension(800, 600));

        // Add event listeners
        sendButton.addActionListener(this);
        messageField.addActionListener(this);
        messageField.setPreferredSize(new Dimension(200, 25));
        messageField.setColumns(20);

        // Create socket connection
        Socket socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        Thread inputThread = new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        inputThread.start();
    }

    public void actionPerformed(ActionEvent e) {
        // Handle user actions
        String message = messageField.getText();
        out.println(message);
        messageField.setText("");
    }

    public static void main(String[] args) throws IOException {
        String serverAddress;
        int serverPort;

        // Get server address and port from user input
        JTextField addressField = new JTextField(10);
        addressField.setText("127.0.0.1");
        JTextField portField = new JTextField(5);
        portField.setText("8001");
        Object[] fields = {"Server Address: ", addressField, "Port: ", portField};
        int option = JOptionPane.showConfirmDialog(null, fields, "Connect to Server", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            serverAddress = addressField.getText();
            serverPort = Integer.parseInt(portField.getText());
        } else {
            return;
        }

        // Create and show the chat window
        Client game = new Client(serverAddress, serverPort);
        game.pack();
        game.setVisible(true);
    }
}
