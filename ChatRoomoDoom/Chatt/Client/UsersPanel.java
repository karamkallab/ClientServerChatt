package Client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Manages and displays the side panel containing lists of friends and all users.
 * Handles adding, displaying, and removing users from the respective panels.
 */
public class UsersPanel {

    private JScrollPane friendsScrollPane, globalScrollPane;
    private JLabel friends, globalUsers, userNameLabel;
    private JPanel sidePanel, friendPanel, globalUserPanel, userPanel;
    private Client client;
    private MainFrame mainFrame;
    private HashMap<String, JPanel> currentUsers;

    /**
     * Initializes the side panel and sets up the layout for displaying friends and all users.
     */
    public UsersPanel(MainFrame mf, Client client) {
        this.mainFrame = mf;
        this.client = client;
        currentUsers = new HashMap<>();
        startUsersPanel();
    }

    /**
     * Sets up and displays the side panel with sections for friends and all users.
     */
    public void startUsersPanel() {
        sidePanel = new JPanel(null);
        sidePanel.setBounds(470, 0, 270, 480);
        sidePanel.setBackground(Color.LIGHT_GRAY);

        globalUsers = new JLabel("Online Users");
        globalUsers.setBackground(Color.getHSBColor(0.2f, 0.7f, 0.4f));
        globalUsers.setBounds(70, 330, 340, 50);
        globalUsers.setFont(new Font("Calibri", Font.PLAIN, 24));
        sidePanel.add(globalUsers);

        String[] onlineUsersList = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7", "User 8"};
        JList<String> globalUsersList = new JList<>(onlineUsersList);
        globalUsersList.setFont(new Font("Calibri", Font.PLAIN, 15));

        globalScrollPane = new JScrollPane();
        globalScrollPane.setBounds(5, 370, 260, 250);
        globalScrollPane.setVisible(true);
        sidePanel.add(globalScrollPane);

        friendsScrollPane = new JScrollPane();
        friendsScrollPane.setBounds(5, 50, 260, 250);
        friendsScrollPane.setVisible(true);
        sidePanel.add(friendsScrollPane);

        friends = new JLabel("Friends");
        friends.setBackground(Color.getHSBColor(0.2f, 0.7f, 0.7f));
        friends.setBounds(100, 5, 340, 30);
        friends.setFont(new Font("Calibri", Font.PLAIN, 24));
        sidePanel.add(friends);

        mainFrame.add(sidePanel);
        mainFrame.repaint();
    }

    /**
     * Adds a user to the "All Users" panel with their profile picture and name.
     * Provides an option to add the user as a friend.
     */
    public void addUserToAllUsers(ImageIcon userProfilePic, String userName) {
        if (globalUserPanel == null) {
            globalUserPanel = new JPanel();
            globalUserPanel.setLayout(new BoxLayout(globalUserPanel, BoxLayout.Y_AXIS));
            globalScrollPane.setViewportView(globalUserPanel);
        }

        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        userPanel.setBackground(Color.gray);

        JLabel profileLabel = new JLabel(new ImageIcon(userProfilePic.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        userPanel.add(profileLabel, BorderLayout.WEST);

        JLabel userNameLabel = new JLabel(userName, SwingConstants.CENTER);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 10));
        userPanel.add(userNameLabel, BorderLayout.CENTER);

        JButton detailsButton = new JButton("Add");
        detailsButton.addActionListener(e -> client.addFriend(userName));

        userPanel.add(detailsButton, BorderLayout.EAST);

        globalUserPanel.add(userPanel);
        globalUserPanel.revalidate();
        globalUserPanel.repaint();

        currentUsers.put(userName, userPanel);
    }

    /**
     * Adds a user to the "Friends" panel with their profile picture and name.
     */
    public void addUserToFriends(String name, ImageIcon image) {
        if (friendPanel == null) {
            friendPanel = new JPanel();
            friendPanel.setLayout(new BoxLayout(friendPanel, BoxLayout.Y_AXIS));
            friendsScrollPane.setViewportView(friendPanel);
        }

        userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        userPanel.setBackground(Color.gray);

        JLabel profileLabel = new JLabel(new ImageIcon(image.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        userPanel.add(profileLabel, BorderLayout.WEST);

        userNameLabel = new JLabel(name, SwingConstants.CENTER);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userPanel.add(userNameLabel, BorderLayout.CENTER);

        friendPanel.add(userPanel);
        friendPanel.revalidate();
        friendPanel.repaint();
    }

    /**
     * Removes a user from the "All Users" panel based on their name.
     */
    public void removeFromAllUsers(String name) {
        globalUserPanel.remove(currentUsers.get(name));
        currentUsers.remove(name);
        globalUserPanel.revalidate();
        globalUserPanel.repaint();
    }
}
