package Server;

import Both.MmsMessage;
import java.io.*;
import java.util.ArrayList;

/**
 * Manages the saving and loading of MmsMessage objects to and from a file.
 * This class handles reading messages from a file upon initialization and saving messages
 * to the file whenever a new message is added.
 */
public class SaveFileManager {
    private String filePath;
    private ArrayList<MmsMessage> mmsMessages;

    /**
     * Constructs a SaveFileManager object with the specified file path.
     * It initializes the list of messages and loads existing messages from the file.
     */
    public SaveFileManager(String filePath) {
        this.filePath = filePath;
        this.mmsMessages = new ArrayList<>();
        loadMessagesFromFile();
    }

    /**
     * Loads messages from the file into the mmsMessages list.
     * If the file exists, it reads objects from the file and adds them to the list,
     * handling end-of-file and other potential exceptions gracefully.
     */
    private void loadMessagesFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
                Object obj;
                while ((obj = reader.readObject()) != null) {
                    if (obj instanceof MmsMessage) {
                        mmsMessages.add((MmsMessage) obj);
                    }
                }
            } catch (EOFException e) {
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in reading file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves all current MMS messages to the file.
     * This method overwrites the file with the updated list of messages.
     */
    public void saveFile() {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (MmsMessage message : mmsMessages) {
                writer.writeObject(message);
            }
        } catch (IOException e) {
            System.err.println("Error in saving file: " + e.getMessage());
        }
    }

    /**
     * Adds a new MMS message to the list and immediately saves the updated list to the file.

    public void addNewMmsMessage(MmsMessage message) {
        mmsMessages.add(message);
        saveFile();
    }

    /**
     * Returns a copy of the list of MMS messages.
     * This prevents external modification of the internal list.
     */
    public ArrayList<MmsMessage> getMmsMessages() {
        return new ArrayList<>(mmsMessages);
    }
}
