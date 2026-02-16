package Client;

import Both.ClientUpdateMessage;
import Both.MmsMessage;
import Both.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Represents a client in the chat application that connects to a server, manages user interactions,
 * and handles message sending and receiving.
 * This class extends Thread to handle communication with the server in a separate thread.
 */
public class Client extends Thread {

    private User user;
    private Socket socket;
    private ServerConnection serverConnection;
    private DataManager dataManager;
    private HashMap<String, User> userList;
    private MainFrame mainFrame;
    private String filePath;

    /**
     * Constructs a new Client instance, initializes the user interface, and connects to the server.
     */
    public Client() {
        try {
            mainFrame = new MainFrame(this);
            String userName = mainFrame.requestUserName();
            dataManager = new DataManager();
            boolean exists = readData(userName); // check if the user exists in the saved data
            if (!exists) {
                String imagePath = mainFrame.profilePicChooser();
                user = new User(userName, imagePath); // create a new user
            }
            socket = new Socket("localhost", 4455); // connect to the server
            serverConnection = new ServerConnection(socket);
            serverConnection.sendMessage(new ClientUpdateMessage(user)); // TCP handshake - Clients stretches hand
            Read read = new Read(); // declare the read thread
            read.start();           // start it
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Failed to connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the user list with the provided list of users. Adds new users to the UI if they are not
     * already present.
     */
    private void setUserList(HashMap<String, User> userList) {
        if (this.userList == null) {
            for (HashMap.Entry<String, User> entry : userList.entrySet()) {
                mainFrame.getUserPanel().addUserToAllUsers(entry.getValue().getImage(), entry.getValue().getUserName());
            }
        } else {
            for (HashMap.Entry<String, User> entry : userList.entrySet()) {
                if (!this.userList.containsKey(entry.getKey())) {
                    mainFrame.getUserPanel().addUserToAllUsers(entry.getValue().getImage(), entry.getValue().getUserName());
                }
            }
        }
    }

    /**
     * Updates the friend list with the provided list of friends.
     */
    private void setFriendList(HashMap<String, User> friendList) {
        for (HashMap.Entry<String, User> entry : friendList.entrySet()) {
            mainFrame.getUserPanel().addUserToFriends(entry.getValue().getUserName(), entry.getValue().getImage());
        }
    }

    /**
     * Disconnects the client from the server, closes all resources, and saves the user data.
     */
    public void disconnect() {
        try {
            user.setConnected(false);
            serverConnection.sendMessage(new ClientUpdateMessage(user));
            serverConnection.getOos().close();
            serverConnection.getOis().close();
            socket.close();
            saveData();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error in closing resources");
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the user data to a file. Updates existing data or creates a new entry if necessary.
     */
    private void saveData() {
        HashMap<String, User> savedUsers = new HashMap<>();
        boolean exists = false;
        File file = new File("data.txt"); // check if it saves the data.txt in project <--
        if (!file.isFile()) {
            savedUsers.put(user.getUserName(), user);
            exists = true;
        } else {
            Object object = dataManager.getObject();
            savedUsers = (HashMap<String, User>) object;
            if (savedUsers.containsKey(user.getUserName())) {
                savedUsers.replace(user.getUserName(), savedUsers.get(user.getUserName()), user);
                exists = true;
            }
        }
        if (!exists) {
            savedUsers.put(user.getUserName(), user);
        }
        dataManager.saveObject(savedUsers);
    }

    /**
     * Reads user data from a file and updates the client if the user data exists.
     */
    private boolean readData(String userName) {
        HashMap<String, User> savedUsers;
        boolean exists = false;
        File file = new File("data.txt");
        if (!file.isFile()) {
            return exists;
        } else {
            savedUsers = (HashMap<String, User>) dataManager.getObject();
            if (savedUsers.containsKey(userName)) {
                user = savedUsers.get(userName);
                user.setConnected(true);
                setFriendList(user.getFriendList());
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Sends a text message to the specified list of users.
     */
    public void sendTextMessage(String users) {
        HashMap<String, User> receivers = new HashMap<>();
        String[] tempRecArray = users.split("/"); // To split the string into an array of usernames
        for (String userName : tempRecArray) {
            if (userList.containsKey(userName)) {
                receivers.put(userName, userList.get(userName));
            }
        }
        String text = mainFrame.getMessagePanel().getTextFromMessageField();

        if ((text == null || text.isEmpty()) && (filePath == null || filePath.isEmpty())) {
            mainFrame.showMessage("404 creativity not found, Cannot send an empty message!");
            return; //
        }

        MmsMessage message = new MmsMessage(text, filePath, user, receivers);

        filePath = "";

        serverConnection.sendMessage(message);
    }

    /**
     * Sets the file path for the image to be sent.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Adds a user as a friend if they are not already a friend and are not the current user.
     */
    public void addFriend(String userName) {
        if (!user.getFriendList().containsKey(userName) && !userName.equals(user.getUserName())) {
            User newFriend = userList.get(userName);
            user.addFriend(userName, newFriend);
            mainFrame.getUserPanel().addUserToFriends(newFriend.getUserName(), newFriend.getImage());
        } else if (userName.equals(user.getUserName())) {
            mainFrame.showMessage("I get it! You love yourself, but you can't be friends with yourself.");
        } else {
            mainFrame.showMessage("Sorry, you can't add the same friend twice, but you can add someone else for free!");
        }
    }

    /**
     * Main method to start the client application.
     */
    public static void main(String[] args) {
        Client client = new Client();
    }

    /**
     * A thread that continuously reads messages from the server and updates the chat UI.
     */
    public class Read extends Thread {

        @Override
        public void run() {
            while (true) {
                Object object = serverConnection.readMessage();
                if (object instanceof MmsMessage message) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    LocalDateTime ldt = LocalDateTime.now();
                    String clientTime = dtf.format(ldt);
                    message.setClientTime(clientTime);
                    mainFrame.getMessagePanel().addMessageToChat(message.getSender().getUserName(), message.getSender().getImage(), message.getText(), message.getClientTime(), message.getImage());
                } else if (object instanceof ClientUpdateMessage cu) {
                    setUserList(cu.getUserList());
                    userList = cu.getUserList();
                    if (!cu.getUser().getConnected()) {
                        mainFrame.getUserPanel().removeFromAllUsers(cu.getUser().getUserName());
                    }
                } else {
                    break;
                }
            }
        }
    }
}
