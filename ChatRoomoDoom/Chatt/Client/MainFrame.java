package Client;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Represents the main frame of the chat application, providing the primary user interface.
 * It includes a chat panel, side panel, and handles user interactions with the window.
 */
public class MainFrame extends JFrame {

    private Client client;
    private MessagePanel messagePanel;
    private UsersPanel usersPanel;

    /**
     * Constructs a new MainFrame with the specified client.
     */
    public MainFrame(Client client) {
        this.client = client;
        startChat();
    }

    /**
     * Initializes the frame's properties and components, including setting the title,
     * icon, layout, and adding a window listener to handle window events.
     */
    public void startChat() {
        setTitle("Chat Room of Doom");
        ImageIcon img = new ImageIcon("src/saved_images/CRoD.png"); // it should set Icon for the frame
        setIconImage(img.getImage());
        System.out.println("did it work?");
        setBounds(200, 200, 770, 525);
        messagePanel = new MessagePanel(this, client);
        usersPanel = new UsersPanel(this, client);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                client.disconnect();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        repaint();
    }

    /**
     * Prompts the user to enter their username.
     *
     * @return the entered username
     */
    public String requestUserName() {
        return JOptionPane.showInputDialog(this, "Enter username");
    }

    /**
     * Opens a file chooser dialog to select a profile picture.
     *
     * @return the path of the selected image file, or null if no file is selected
     */
    public String profilePicChooser() {
        String imagePath = null;
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            imagePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        return imagePath;
    }

    /**
     * Displays a message dialog with the specified message.
     */
    public void showMessage(String mess) {
        JOptionPane.showMessageDialog(this, mess);
    }

    /**
     * Gets the chat panel associated with this frame.
     *
     * @return the chat panel
     */
    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    /**
     * Gets the side panel associated with this frame.
     *
     * @return the User panel
     */
    public UsersPanel getUserPanel() {
        return usersPanel;
    }
}
