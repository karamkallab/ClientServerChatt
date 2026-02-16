package Both;

import java.util.HashMap;

/**
 * Represents an update for a client in the messaging system.
 * This class holds the information about a user and a list of users.
 */
public class ClientUpdateMessage implements Message {
    private User user;
    private HashMap<String, User> userList;

    /**
     * Constructs a new ClientUpdateMessage with the specified user.
     */
    public ClientUpdateMessage(User user) {
        this.user = user;
    }

    /**
     * Returns the user associated with this ClientUpdateMessage.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the list of users associated with this ClientUpdateMessage.
     */
    public HashMap<String, User> getUserList() {
        return userList;
    }

    /**
     * Sets the list of users for this ClientUpdateMessage.
     */
    public void setUserList(HashMap<String, User> userList) {
        this.userList = userList;
    }
}
