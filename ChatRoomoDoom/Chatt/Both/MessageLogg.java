package Both;

import Server.SaveFileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages global chat messages and interactions with the message file storage.
 * This class provides methods to set up the chat, add messages, save messages, and retrieve messages.
 */
public class MessageLogg {
    private static ArrayList<MmsMessage> messages = new ArrayList<>();
    static SaveFileManager fileManager;

    /**
     * Sets up the global chat by initializing the file manager and loading existing messages from the file.
     */
    public static void setup() {
        fileManager = new SaveFileManager("SaveFile.txt");
        messages = fileManager.getMmsMessages();
    }

    /**
     * Adds a new message to the global chat and saves the updated message list.
     */
    public static void addMessage(MmsMessage message) {
        messages.add(message);
        MessageLogg.save();
    }

    /**
     * Saves the current state of the global chat messages to the file.
     */
    public static void save() {
        fileManager.saveFile();
    }

    /**
     * Returns the list of all messages in the global chat.
     */
    public static List<MmsMessage> getMessages() {
        return messages;
    }
}
