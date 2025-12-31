package com.snake.game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.net.URL;

public class GameFrame extends JFrame{

    // Store ref to current panel
    private JPanel currentPanel;

    // Constructor
    public GameFrame() {
        // Set window title
        setTitle("Snake Game");

        // Window cannot be resized
        setResizable(false);

        // Exit program when window closes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set window icon
        setWindowIcon();

        // Show the menu first
        showMenu();

        // Center window on screen
        setLocationRelativeTo(null);

        // Make window visible
        setVisible(true);
    }

    /**
     * Sets the window icon
     */
    private void setWindowIcon() {
        try {
            URL iconURL = getClass().getResource("/images/snake_icon.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                setIconImage(icon.getImage());
            } else {
                System.out.println("Icon not found!");
            }
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
        }
    }

    /**
     * Shows the main menu
     */
    public void showMenu() {
        // Remove current panel if exists
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Create and add menu panel
        currentPanel = new MenuPanel(this);
        add(currentPanel);

        // Resize window to fit panel
        pack();

        // Refresh the window
        revalidate();
        repaint();
    }

    /**
     * Starts the game - this is called when PLAY button is clicked
     */
    public void startGame() {
        // Remove current panel
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Create and add game panel
        GamePanel gamePanel = new GamePanel(this);
        currentPanel = gamePanel;
        add(currentPanel);

        // Resize the window to fit panel
        pack();

        // Refresh the window
        revalidate();
        repaint();

        // Request focus for keyboard input
        gamePanel.requestFocusInWindow();
    }

    /**
     * Shows the settings screen
     */
    public void showSettings() {
        // Remove current panel
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Create and add settings panel
        currentPanel = new SettingsPanel(this);
        add(currentPanel);

        // Resize the window to fit panel
        pack();

        // Refresh the window
        revalidate();
        repaint();
    }

    /**
     *  Shows the controls screen
     */
    public void showControls() {
        // Remove current panel
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Create and add controls panel
        currentPanel = new ControlsPanel(this);
        add(currentPanel);

        // Resize the window to fit panel
        pack();

        // Refresh the window
        revalidate();
        repaint();
    }
}