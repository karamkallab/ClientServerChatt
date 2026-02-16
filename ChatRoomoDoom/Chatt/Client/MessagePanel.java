package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the chat panel in the client application where users can send and receive messages and images.
 * This panel includes input fields, buttons, and message display areas.
 */
public class MessagePanel {
    private JPanel messagePanel, singleMessagePanel, imageTimePanel;
    private MainFrame mainFrame;
    private JButton sendButton, sendImageButton;
    private JTextField messageField;
    private JScrollPane messageWindow;
    private Client client;

    /**
     * Constructs a new MessagePanel with the specified MainFrame and Client.
     */
    public MessagePanel(MainFrame mf, Client client) {
        this.mainFrame = mf;
        this.client = client;
        startMessagePanel();
    }

    /**
     * Initializes and sets up the chat panel with its components.
     * This includes setting up the layout, buttons, and message display area.
     */
    public void startMessagePanel() {
        messagePanel = new JPanel(null);
        messagePanel.setBackground(Color.getHSBColor(0.2f, 0.7f, 0.4f)); // color =
        messagePanel.setBounds(10, 0, 450, 480);
        messagePanel.setLayout(null);
        messagePanel.setVisible(true);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE);

        messageWindow = new JScrollPane(messagePanel);
        messageWindow.setBounds(5, 5, 440, 400);
        messageWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.messagePanel.add(messageWindow);

        messageField = new JFormattedTextField();
        messageField.setBounds(5, 410, 300, 50);
        this.messagePanel.add(messageField);

        setButtons();
        mainFrame.add(this.messagePanel);
        mainFrame.repaint();
    }

    /**
     * Configures the buttons for sending text messages and images.
     * Sets action listeners for each button to handle user interactions.
     */
    private void setButtons() {
        sendButton = new JButton("Send");
        sendButton.setBackground(Color.GREEN);
        sendButton.setBounds(310, 410, 60, 50);
        sendButton.addActionListener(e -> {
            String receivers = JOptionPane.showInputDialog("Please enter the receivers name (or names separated by '/')");
            client.sendTextMessage(receivers);
        });
        messagePanel.add(sendButton);

        sendImageButton = new JButton("Image");
        sendImageButton.setBackground(Color.CYAN);
        sendImageButton.setBounds(380, 410, 60, 50);
        sendImageButton.addActionListener(e -> {
            String filePath = mainFrame.profilePicChooser();
            if (filePath != null) {
                client.setFilePath(filePath);
                String receivers = JOptionPane.showInputDialog("Please enter the receivers name (or names separated by '/')");
                client.sendTextMessage(receivers);
            }
        });
        messagePanel.add(sendImageButton);
    }

    /**
     * Adds a message to the chat display area.
     * Updates the UI to show the new message along with any associated image and timestamp.
     */
    public void addMessageToChat(String name, ImageIcon image, String message, String currentTime, ImageIcon imageToSend) {
        if (message != null && !message.isEmpty()) {
            singleMessagePanel = new JPanel();
            singleMessagePanel.setLayout(new BoxLayout(singleMessagePanel, BoxLayout.X_AXIS));
            singleMessagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            singleMessagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            imageTimePanel = new JPanel();
            imageTimePanel.setLayout(new BoxLayout(imageTimePanel, BoxLayout.Y_AXIS));
            imageTimePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            ImageIcon originalIcon = image;
            Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageTimePanel.add(imageLabel);

            JLabel timeLabel = new JLabel(currentTime);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageTimePanel.add(timeLabel);

            singleMessagePanel.add(imageTimePanel);

            JLabel textLabel = new JLabel(name + ": " + message);
            textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            singleMessagePanel.add(textLabel);

            JPanel messageContainer = (JPanel) messageWindow.getViewport().getView();
            messageContainer.add(singleMessagePanel);
            messageContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            messageContainer.revalidate();
            messageContainer.repaint();

            JScrollBar verticalBar = messageWindow.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        }

        // Display image if present
        if (imageToSend != null) {
            sendImage(name, image, currentTime, imageToSend);
        }
    }

    /**
     * Retrieves the text from the message input field and clears it.
     *
     * @return the text from the input field
     */
    public String getTextFromMessageField() {
        String text = messageField.getText();
        messageField.setText("");
        return text;
    }

    /**
     * Displays an image in the chat panel.
     * Adds the image along with the sender's profile image and timestamp to the chat display area.
     */
    public void sendImage(String name, ImageIcon pic, String time, ImageIcon imageToSend) {
        ImageIcon imageIcon = imageToSend;
        Image scaledImage = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        singleMessagePanel = new JPanel();
        singleMessagePanel.setLayout(new BoxLayout(singleMessagePanel, BoxLayout.X_AXIS));
        singleMessagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        singleMessagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        imageTimePanel = new JPanel();
        imageTimePanel.setLayout(new BoxLayout(imageTimePanel, BoxLayout.Y_AXIS));
        imageTimePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon profileIcon = new ImageIcon(pic.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(profileIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageTimePanel.add(imageLabel);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageTimePanel.add(timeLabel);

        singleMessagePanel.add(imageTimePanel);

        JLabel textLabel = new JLabel(name + ": ");
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sendImageLabel = new JLabel(scaledIcon);
        sendImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        singleMessagePanel.add(textLabel);
        singleMessagePanel.add(sendImageLabel);

        JPanel messageContainer = (JPanel) messageWindow.getViewport().getView();
        messageContainer.add(singleMessagePanel);
        messageContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        messageContainer.revalidate();
        messageContainer.repaint();

        JScrollBar verticalBar = messageWindow.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }
}
