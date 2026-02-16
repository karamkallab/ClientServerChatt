package Client;

import java.io.*;

/**
 * Manages the persistence of objects by saving them to and loading them from a file.
 * This class provides methods to save and retrieve objects using serialization.
 */
public class DataManager {

    /**
     * Saves the provided object to a file named "data.txt" using serialization.
     */
    public void saveObject(Object object) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.txt"));
            oos.writeObject(object);
            oos.reset();
            oos.close(); // Ensure the stream is closed after writing
        } catch (IOException e) {
            System.out.println("It was not possible to save the object");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves an object from the file named "data.txt" using deserialization.
     */
    public Object getObject() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.txt"));
            Object object = ois.readObject();
            ois.close(); // Ensure the stream is closed after reading
            return object;
        } catch (IOException e) {
            System.out.println("It could not read the object");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("The class of the object could not be found");
            throw new RuntimeException(e);
        }
    }
}
