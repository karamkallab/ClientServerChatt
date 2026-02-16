package Client;

import java.io.*;
import java.net.Socket;

/**
 * Manages the connection to the server by handling the input and output streams
 * for sending and receiving objects over a socket connection.
 */
public class ServerConnection {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Constructs a ServerConnection using the specified socket.
     * Initializes the input and output streams for communication with the server.
     */
    public ServerConnection(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error in initializing streams");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an object to the server using the output stream.
     */
    public void sendMessage(Object object) {
        try {
            oos.writeObject(object);
            oos.reset();
        } catch (IOException e) {
            System.out.println("Error in sending message");
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads an object from the server using the input stream.
     *
     * @return the object read from the server, or null if an I/O error occurs
     */
    public Object readMessage() {
        try {
            return ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error in reading message");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the ObjectOutputStream used for sending objects to the server.
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * Gets the ObjectInputStream used for receiving objects from the server.
     */
    public ObjectInputStream getOis() {
        return ois;
    }
}
