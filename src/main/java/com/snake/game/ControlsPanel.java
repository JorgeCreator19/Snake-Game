package com.snake.game;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.snake.utils.Constants;
import com.snake.utils.SoundManager;

public class ControlsPanel extends JPanel{
    
    private GameFrame gameFrame;
    private JButton backButton;

    // Constructor
    public ControlsPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // Set panel size
        setPreferredSize(new Dimension(Constants.MENU_PANEL_WIDTH, Constants.MENU_PANEL_HEIGHT));
        setBackground(new Color(30, 30, 30));

        // Use null layout for manual positioning
        setLayout(null);

        // Create back button
        createBackButton();
    }

    /**
     * Creates the BACK button
     */
    private void createBackButton() {
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setFocusable(true);
        
        // Position in bottom of panel
        backButton.setBounds(Constants.MENU_PANEL_WIDTH - 420, Constants.MENU_PANEL_HEIGHT - 50, 80, 30);

        // Add action listener to go back to settings
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.getInstance().playClick();
                gameFrame.showSettings();
            }
        });
        
        add(backButton);
    }

    /**
     * Custom painting for controls info
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw title
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        String title = "CONTROLS";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (Constants.MENU_PANEL_WIDTH - titleWidth) / 2, 60);
        
        // Draw controls info
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        
        int startY = 130;
        int lineHeight = 40;
        int leftX = 100;
        int rightX = 420;
        
        // Movement controls
        g2d.setColor(Color.GREEN);
        g2d.drawString("MOVEMENT", leftX, startY);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        
        g2d.drawString("Move Up:", leftX, startY + lineHeight);
        g2d.drawString("W  or  ↑", rightX, startY + lineHeight);
        
        g2d.drawString("Move Down:", leftX, startY + lineHeight * 2);
        g2d.drawString("S  or  ↓", rightX, startY + lineHeight * 2);
        
        g2d.drawString("Move Left:", leftX, startY + lineHeight * 3);
        g2d.drawString("A  or  ←", rightX, startY + lineHeight * 3);
        
        g2d.drawString("Move Right:", leftX, startY + lineHeight * 4);
        g2d.drawString("D  or  →", rightX, startY + lineHeight * 4);
        
        // Game controls
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("GAME CONTROLS", leftX, startY + lineHeight * 6);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        
        g2d.drawString("Pause:", leftX, startY + lineHeight * 7);
        g2d.drawString("P", rightX, startY + lineHeight * 7);
        
        g2d.drawString("Restart:", leftX, startY + lineHeight * 8);
        g2d.drawString("R  (when game over)", rightX, startY + lineHeight * 8);
        
        g2d.drawString("Back to Menu:", leftX, startY + lineHeight * 9);
        g2d.drawString("ESC", rightX, startY + lineHeight * 9);
    }
}