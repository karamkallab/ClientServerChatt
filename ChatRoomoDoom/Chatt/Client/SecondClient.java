package Client;

/**
 * This class makes it possible to chat with yourself.
 */
public class SecondClient {

    /**
     * The main method that starts a new thread to run an instance of the Client class.
     * This allows the creation of a second client in the application.
     */
    public static void main(String[] args) {
        // Start second client or add as many as I wish until it crashes
        new Thread(() -> new Client()).start();

    }
}
