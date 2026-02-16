package Server;

import Both.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the lifecycle and operations of the server, including handling client connections,
 * broadcasting messages, and providing server functionalities.
 */
public class Server {
    private static HashMap<String, ClientHandler> clients = new HashMap<>();

    /**
     * The entry point of the server application. Starts the server and initializes necessary components.
     */
    public static void main(String[] args) {
        startServer();
    }

    /**
     * Initializes the server user interface and starts the client reception process.
     * Also sets up the global chat system.
     */
    public static void startServer() {
        ServerViewer window = new ServerViewer();
        window.startServerUi();

        new ClientReceiver(4455);
        MessageLogg.setup();
    }

    /**
     * Retrieves the map of connected clients.
     *
     * @return a HashMap where keys are usernames and values are ClientHandler objects
     */
    public static HashMap<String, ClientHandler> getClients() {
        return clients;
    }

    /**
     * Retrieves all currently online users.
     *
     * @return a HashMap of usernames and corresponding User objects for online users
     */
    public static HashMap<String, User> getAllOnlineUsers() {
        HashMap<String, User> tempList = new HashMap<>();
        for (ClientHandler client : clients.values()) {
            if (client.getUser().getConnected()) {
                tempList.put(client.getUser().getUserName(), client.getUser());
            }
        }
        return tempList;
    }

    /**
     * Processes a new Message received by the server. Depending on the type of message,
     * it delegates the processing to appropriate methods.
     */
    public synchronized static void newMessage(Message message) {
        if (message instanceof MmsMessage) {
            MessageLogg.addMessage((MmsMessage) message);
            processMmsMessage((MmsMessage) message);
        } else if (message instanceof ClientUpdateMessage) {
            ((ClientUpdateMessage) message).setUserList(getAllOnlineUsers());
            processUpdateMessage((ClientUpdateMessage) message);
        }
    }

    /**
     * Processes a MmsMessage by adding a server timestamp and sending it to appropriate clients.
     */
    public static void processMmsMessage(MmsMessage message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String serverTime = dtf.format(LocalDateTime.now());
        message.setServerTime(serverTime);

        for (User receiver : message.getReceivers().values()) {
            if (!receiver.getUserName().equals(message.getSender().getUserName()) && clients.containsKey(receiver.getUserName())) {
                clients.get(receiver.getUserName()).sendMessage(message);
            }
        }
        clients.get(message.getSender().getUserName()).sendMessage(message);
    }

    /**
     * Processes a ClientUpdateMessage by updating client statuses and notifying all clients.
     */
    public static void processUpdateMessage(ClientUpdateMessage message) {
        if (message.getUser().getConnected()) {
            clientJoined(message);
        } else {
            clientLeft(message);
        }
    }

    /**
     * Handles the event when a new client joins. Notifies all connected clients of the new client.
     */
    public static void clientJoined(ClientUpdateMessage message) {
        for (ClientHandler client : clients.values()) {
            if (client.getUser().getConnected()) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Handles the event when a client leaves. Updates client statuses and notifies all connected clients.
     */
    public static void clientLeft(ClientUpdateMessage message) {
        if (clients.containsKey(message.getUser().getUserName())) {
            clients.get(message.getUser().getUserName()).getUser().setConnected(false);
        }

        for (ClientHandler client : clients.values()) {
            if (client.getUser().getConnected()) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Retrieves messages within a specified date range.
     *
     * @param fromDate the start date in "yyyy/MM/dd HH:mm" format
     * @param toDate the end date in "yyyy/MM/dd HH:mm" format
     * @return a list of MmsMessage objects within the date range
     */
    public static ArrayList<MmsMessage> getMessagesBasedOnDate(String fromDate, String toDate) {
        if (!validateDates(fromDate, toDate)) {
            return new ArrayList<>();
        }

        for (int i = 0; i < MessageLogg.getMessages().size(); i++) {
            if (checkMessageDate(fromDate, MessageLogg.getMessages().get(i))) {
                for (int u = i; u < MessageLogg.getMessages().size(); u++) {
                    if (checkMessageDate(toDate, MessageLogg.getMessages().get(u))) {
                        return getMmsMessagesInterval(i, u);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Checks if a message's date is within or after the specified date.
     *
     * @param date the date to check against in "yyyy/MM/dd HH:mm" format
     * @param message the MmsMessage to check
     * @return true if the message's date is on or after the specified date; false otherwise
     */
    public static boolean checkMessageDate(String date, MmsMessage message) {
        String tempMessageDate = message.getServerTime();
        return tempMessageDate.compareTo(date) >= 0;
    }

    /**
     * Validates the date format of the given date strings.
     *
     * @param fromDate the start date in "yyyy/MM/dd HH:mm" format
     * @param toDate the end date in "yyyy/MM/dd HH:mm" format
     * @return true if both dates are valid; false otherwise
     */
    public static boolean validateDates(String fromDate, String toDate) {
        try {
            LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
            LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
            return true;
        } catch (Exception e) {
            System.out.println("Invalid date format");
            return false;
        }
    }

    /**
     * Retrieves a list of MMS messages between specified start and end indices.
     *
     * @param startIndex the starting index of the messages
     * @param endIndex the ending index of the messages
     * @return a list of MmsMessage objects between the specified indices
     */
    public static ArrayList<MmsMessage> getMmsMessagesInterval(int startIndex, int endIndex) {
        ArrayList<MmsMessage> temp = new ArrayList<>(MessageLogg.getMessages().subList(startIndex, endIndex + 1));
        return temp;
    }
}
