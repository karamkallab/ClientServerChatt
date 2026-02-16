package Server;

import Both.MmsMessage;
import Both.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides the user interface for the server, allowing for message filtering and display.
 * This class sets up and manages the server's graphical user interface, including components
 * for filtering messages based on date ranges and displaying them in a scrollable panel.
 */
public class ServerViewer {
    private JFrame window;
    private JButton filterButton;
    private JPanel mainPanel, panelInScroll;
    private JLabel startLabel, endLabel;
    private JTextField textField1, textField2;
    private JScrollPane scrollPanel;
    private ArrayList<JLabel> labelList = new ArrayList<>();

    /**
     * Initializes and displays the server user interface.
     */
    public void startServerUi() {
        window = new JFrame();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setTitle("Server");

        setupComponents();
        setupComponentPositions();

        window.repaint();
        window.pack();
    }

    /**
     * Sets up the components for the user interface, including labels, text fields, buttons,
     * and panels.
     */
    public void setupComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1000, 300));

        startLabel = new JLabel("Start date: ");
        endLabel = new JLabel("End date: ");

        filterButton = new JButton("Search");
        textField1 = new JTextField("yyyy/MM/dd HH:mm"); // Format for the date search
        textField2 = new JTextField("yyyy/MM/dd HH:mm"); // and other date because it's "from" and "to"

        panelInScroll = new JPanel();
        panelInScroll.setLayout(null);
        panelInScroll.setPreferredSize(new Dimension(960, 0));
        panelInScroll.setBackground(Color.GRAY);

        scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(panelInScroll);

        mainPanel.add(startLabel);
        mainPanel.add(endLabel);
        mainPanel.add(filterButton);
        mainPanel.add(textField1);
        mainPanel.add(textField2);
        mainPanel.add(scrollPanel);

        window.add(mainPanel);

        // Add action listener to the filter button
        filterButton.addActionListener(e -> {
            // Get input times from text fields
            String startDate = textField1.getText();
            String endDate = textField2.getText();

            // Get filtered messages based on the date range
            ArrayList<MmsMessage> filteredMessages = Server.getMessagesBasedOnDate(startDate, endDate);

            // Display the filtered messages
            addTextMessages(filteredMessages);
        });
    }

    /**
     * Sets up the positions and sizes of the UI components within the main panel.
     */
    public void setupComponentPositions() {
        filterButton.setBounds(890, 10, 100, 30);
        startLabel.setBounds(10, 10, 50, 30);
        endLabel.setBounds(450, 10, 50, 30);

        textField1.setBounds(70, 10, 350, 30);
        textField2.setBounds(520, 10, 350, 30);

        scrollPanel.setBounds(10, 50, 980, 200);
        panelInScroll.setBounds(0, 0, 980, 1000);
    }

    /**
     * Adds text messages to the scrollable panel, creating a label for each message.
     * The messages are displayed with images (if available) and detailed information.
     *
     * @param allMessages a list of MmsMessage objects to be displayed
     */
    public void addTextMessages(ArrayList<MmsMessage> allMessages) {
        panelInScroll.removeAll();
        panelInScroll.setPreferredSize(new Dimension(960, 0));
        labelList.clear();

        for (MmsMessage message : allMessages) {
            JLabel newLabel = new JLabel();
            try {
                // Display message image if it exists
                Image tempImage = message.getImage().getImage();
                tempImage = tempImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                newLabel.setIcon(new ImageIcon(tempImage));
            } catch (Exception e) {
                System.out.println("No image available for this message.");
            }

            // Prepare log message
            String log = "Date: " + message.getServerTime() + " | Message: " + message.getText() +
                    " | From: " + message.getSender().getUserName() + " | To: ";

            // Append receivers to the log message
            for (User receiver : message.getReceivers().values()) {
                log += receiver.getUserName() + ", ";
            }

            // Set the label text and position
            newLabel.setText(log);
            newLabel.setBounds(0, labelList.size() * 51, 960, 50);

            // Add the label to the list and the panel
            labelList.add(newLabel);
        }

        // Add labels to the scrollable panel and update its preferred size
        for (int i = 0; i < labelList.size(); i++) {
            panelInScroll.add(labelList.get(i));
            panelInScroll.setPreferredSize(new Dimension(960, (i + 1) * 51));
        }

        // Refresh the UI
        window.pack();
        window.repaint();
    }
}
