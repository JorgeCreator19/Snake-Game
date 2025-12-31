package com.snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.snake.utils.Constants;
import com.snake.utils.SoundManager;

public class MenuPanel extends JPanel{
    
    // Ref to main game frame
    private GameFrame gameFrame;

    // Buttons
    private JButton playButton;
    private JButton exitButton;

    // Constructor
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // Set panel size
        setPreferredSize(new Dimension(Constants.MENU_PANEL_WIDTH, Constants.MENU_PANEL_HEIGHT));
        setBackground(new Color(30, 30, 30));

        // Use GridBagLayout to center buttons
        setLayout(new GridBagLayout());

        // Create buttons
        createButtons();
    }

    /**
     * Creates and adds buttons to the panel
     */
    private void createButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 30, 10); // Spacing around buttons
        gbc.gridx = 0;

        // PLAY button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));
        playButton.setPreferredSize(new Dimension(150, 50));
        playButton.setFocusable(false); // This prevents button from stealing focus

        // Add action listener to start the game
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playClick(); // Play click sound
                gameFrame.startGame(); // Start the game
            }
        });

        gbc.gridy = 0;
        add(playButton, gbc);

        // SETTINGS button
        JButton settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Arial", Font.BOLD, 24));
        settingsButton.setPreferredSize(new Dimension(150, 50));
        settingsButton.setFocusable(false);

        // Add action listener to show settings panel
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playClick();
                gameFrame.showSettings();
            }
        });

        gbc.gridy = 1;
        add(settingsButton, gbc);

        // EXIT button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.setFocusable(false); // This prevents button from stealing focus

        // Add acton listener to exit the game
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playClick();
                System.exit(0); // Exit the program
            }
        });

        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    /**
     * Draw the title game inside menu
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw tittle "SNAKE GAME"
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));

        String title = "SNAKE GAME";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        int x = (Constants.MENU_PANEL_WIDTH - titleWidth) / 2;

        g2d.drawString(title, x, 130);
    }
}