package Server;

import Both.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the connection and communication for a single client.
 * Manages the client's state, message queue, and interaction with other clients.
 */
public class ClientHandler {
    private User user;
    private ArrayList<Message> messagesToSend = new ArrayList<>();
    private ClientConnection connection;

    /**
     * Constructs a ClientHandler and checks if the client is new.
     */
    public ClientHandler(Socket client) {
        checkIfClientIsNew(client);
    }

    /**
     * Sends a message to the client. The message is added to the queue and sent when possible.
     */
    public synchronized void sendMessage(Message message) {
        try {
            messagesToSend.add(message);
            connection.sendWaitingMessages();
        } catch (Exception e) {
            System.out.println("Error in sending message");
        }
    }

    /**
     * Retrieves the User associated with this client handler.
     *
     * @return the User of the client
     */
    public User getUser() {
        return user;
    }

    /**
     * Restarts the client handler with a new user, initializing a new ClientConnection.
     */
    public void restart(ObjectInputStream reader, ObjectOutputStream writer, User newUser) {
        System.out.println("New client: " + newUser.getUserName());
        this.user = newUser;

        try {
            connection = new ClientConnection(reader, writer, this);
            connection.start();

            ClientUpdateMessage clientUpdate = new ClientUpdateMessage(user);
            Server.getClients().put(user.getUserName(), this);
            Server.newMessage(clientUpdate);
        } catch (Exception e) {
            System.out.println("Error in client handler restart");
        }
    }

    /**
     * Restarts the client handler with existing user information, initializing a new ClientConnection.
     */
    public void restart(ObjectInputStream reader, ObjectOutputStream writer) {
        System.out.println("Old client: " + user.getUserName());
        try {
            connection = new ClientConnection(reader, writer, this);
            user.setConnected(true);
            connection.start();

            ClientUpdateMessage clientUpdate = new ClientUpdateMessage(user);
            Server.newMessage(clientUpdate);

            connection.sendWaitingMessages();
        } catch (Exception e) {
            System.out.println("Error in restarting client");
        }
    }

    /**
     * Checks if the client is new or reconnecting and initializes the appropriate connection.
     */
    public void checkIfClientIsNew(Socket client) {
        try {
            ObjectInputStream reader = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(client.getOutputStream());

            ClientUpdateMessage message = (ClientUpdateMessage) reader.readObject();
            User tempUser = message.getUser();

            if (Server.getClients().containsKey(tempUser.getUserName())) {
                Server.getClients().get(tempUser.getUserName()).restart(reader, writer);
                return;
            }

            restart(reader, writer, tempUser);

        } catch (Exception e) {
            System.out.println("Error in client handler");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of messages waiting to be sent to the client.
     *
     * @return the ArrayList of Message objects waiting to be sent
     */
    public ArrayList<Message> getMessagesToSend() {
        return messagesToSend;
    }
}
