package Server;

import Both.Message;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles the communication between the server and a connected client.
 * Manages receiving messages from the client and sending messages to the client.
 */
public class ClientConnection extends Thread {
    private ClientHandler client;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    /**
     * Constructs a ClientConnection with the specified input and output streams and client handler.
     */
    public ClientConnection(ObjectInputStream reader, ObjectOutputStream writer, ClientHandler client) {
        this.client = client;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Continuously reads messages from the client and processes them.
     * Messages are forwarded to the server for further handling.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Object object = reader.readObject();
                Server.newMessage((Message) object);
            }
        } catch (Exception e) {
            System.out.println("Error in reading message, disconnected?");
        }
    }

    /**
     * Sends any waiting messages to the client if they are connected.
     * This method is synchronized to ensure thread safety when sending messages.
     */
    public synchronized void sendWaitingMessages() {
        try {
            if (client.getUser().getConnected()) {
                for (int i = 0; i < client.getMessagesToSend().size(); i++) {
                    writer.writeObject(client.getMessagesToSend().remove(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Error in client handler sending waiting messages");
        }
    }
}
