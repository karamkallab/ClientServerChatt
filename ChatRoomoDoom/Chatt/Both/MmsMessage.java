package Both;

import javax.swing.*;
import java.util.HashMap;

/**
 * Represents MMS message in the chat system, which can include text, an optional image, a sender, and a list of receivers.
 * This class implements the Message interface.
 */
public class MmsMessage implements Message {

    private String text;
    private ImageIcon image;
    private User sender;
    private HashMap<String, User> receivers;
    private String serverTime;
    private String clientTime;

    /**
     * Constructs a new MMS Message with the specified details.
     */
    public MmsMessage(String text, String imagePath, User sender, HashMap<String, User> receivers) {
        this.text = text;
        if (imagePath != null && !imagePath.isEmpty()) {
            this.image = new ImageIcon(imagePath);
        }
        this.sender = sender;
        this.receivers = receivers;
    }

    /**
     * Returns the text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the image associated with the message.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * Returns the user who sent the message.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Returns the server time when the message was sent.
     */
    public String getServerTime() {
        return serverTime;
    }

    /**
     * Sets the server time for the message.
     */
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    /**
     * Returns the client time when the message was sent.
     */
    public String getClientTime() {
        return clientTime;
    }

    /**
     * Sets the client time for the message.
     */
    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    /**
     * Returns the list of users who will receive the message.
     */
    public HashMap<String, User> getReceivers() {
        return receivers;
    }
}
