package Server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listens for incoming client connections and handles them by creating a new ClientHandler.
 * This class runs on a separate thread and accepts connections from clients, forwarding them to the ClientHandler.
 */
public class ClientReceiver extends Thread {
    private ServerSocket serverSocket;

    /**
     * Constructs a ClientReceiver object and starts listening on the specified port.
     */
    public ClientReceiver(int port) {
        try {
            serverSocket = new ServerSocket(port);
            start();
        } catch (Exception e) {
            System.out.println("Error in starting server");
        }
    }

    /**
     * Runs the thread to accept client connections in a continuous loop.
     * For each connection, a new ClientHandler is created to manage the client's communication.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket newClient = serverSocket.accept();
                System.out.println("Connected");
                new ClientHandler(newClient);
            } catch (Exception e) {
                System.out.println("Error in accepting client, not connected");
                e.printStackTrace();
            }
        }
    }
}
