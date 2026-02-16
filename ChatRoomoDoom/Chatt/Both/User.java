package Both;

import javax.swing.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a user in the chat system with a username, image, connection status, and a list of friends.
 * This class implements Serializable to allow user data to be serialized.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Ensure it matches with saved data
    private String userName;
    private ImageIcon image;
    private boolean connected;
    private HashMap<String, User> friendList;

    /**
     * Constructs a new User with the specified username and image path.
     */
    public User(String userName, String imagePath) {
        this.userName = userName;
        image = new ImageIcon(imagePath);
        connected = true;
        friendList = new HashMap<>();
    }

    /**
     * Returns the username of this user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the image associated with this user.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * Returns the connection status of this user.
     */
    public boolean getConnected() {
        return connected;
    }

    /**
     * Sets the connection status of this user.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Returns the friend list of this user.
     */
    public HashMap<String, User> getFriendList() {
        return friendList;
    }

    /**
     * Adds a new friend to this user's friend list.
     */
    public void addFriend(String userName, User newFriend) {
        friendList.put(userName, newFriend);
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User)
            return userName.equals(((User) obj).getUserName());
        return false;
    }
}
