package com.snake.game;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.snake.utils.Constants;
import com.snake.utils.SoundManager;

public class SettingsPanel extends JPanel{
    
    private GameFrame gameFrame;
    private SoundManager soundManager;

    // UI Components
    private JSlider musicSlider;
    private JSlider sfxSlider;
    private JCheckBox muteCheckbox;
    private JButton backButton;

    // Constructor 
    public SettingsPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.soundManager = SoundManager.getInstance();

        // Set panel size
        setPreferredSize(new Dimension(Constants.MENU_PANEL_WIDTH, Constants.MENU_PANEL_HEIGHT));
        setBackground(new Color(30, 30, 30));

        // Use GridBagLayout
        setLayout(new GridBagLayout());

        // Create UI components
        createComponents();
    }

    /**
     * 
     */
    private void createComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(17, 15, 2, 15);
        gbc.gridx = 0;

        // MUSIC VOLUME
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel musicLabel = new JLabel("Music Volume");
        musicLabel.setFont(new Font("Arial", Font.BOLD, 18));
        musicLabel.setForeground(Color.BLACK);
        add(musicLabel, gbc);

        gbc.gridy = 1;
        musicSlider = new JSlider(0, 100, (int)(soundManager.getMusicVolume() * 100));
        musicSlider.setPreferredSize(new Dimension(200, 40));
        musicSlider.setBackground(new Color(30, 30, 30));
        musicSlider.setForeground(Color.GREEN);

        // Add change listener to update volume
        musicSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = musicSlider.getValue() / 100.0f;
                soundManager.setMusicVolume(volume);
            }
        });
        add(musicSlider, gbc);

        // SFX VOLUME
        gbc.gridy = 2;
        JLabel sfxLabel = new JLabel("Sound Effects Volume");
        sfxLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sfxLabel.setForeground(Color.BLACK);
        add(sfxLabel, gbc);

        gbc.gridy = 3;
        sfxSlider = new JSlider(0, 100, (int) (soundManager.getSfxVolume() * 100));
        sfxSlider.setPreferredSize(new Dimension(200, 40));
        sfxSlider.setBackground(new Color(30, 30, 30));
        sfxSlider.setForeground(Color.GREEN);

        // Add change listener to update volume
        sfxSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume = sfxSlider.getValue() / 100.0f;
                soundManager.setSfxVolume(volume);
            }
        });
        add(sfxSlider, gbc);

        // MUTE CHECKBOX
        gbc.gridy = 4;
        muteCheckbox = new JCheckBox("Mute all Sounds");
        muteCheckbox.setFont(new Font("Arial", Font.BOLD, 16));
        muteCheckbox.setForeground(Color.WHITE);
        muteCheckbox.setBackground(new Color(30, 30, 30));
        muteCheckbox.setSelected(soundManager.isMuted());
        muteCheckbox.setFocusable(false);

        // Add action listener to toggle mute
        muteCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundManager.setMuted(muteCheckbox.isSelected());
            }
        });
        add(muteCheckbox, gbc);

        // CONTROLS BUTTON
        gbc.gridy = 5;
        gbc.insets = new Insets(25, 15, 10, 15);
        JButton controlsButton = new JButton("Controls");
        controlsButton.setFont(new Font("Arial", Font.BOLD, 20));
        controlsButton.setPreferredSize(new Dimension(150, 45));
        controlsButton.setFocusable(false);

        // Add action listener to show controls panel
        controlsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            soundManager.playClick();
            gameFrame.showControls();
            }
        });
        add(controlsButton, gbc);

        // BACK BUTTON
        gbc.gridy = 6;
        gbc.insets = new Insets(25, 15, 10, 15);
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setPreferredSize(new Dimension(150, 45));
        backButton.setFocusable(false);

        // Add action listener to go back to menu
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundManager.playClick();
                gameFrame.showMenu();
            }
        });
        add(backButton, gbc);
    }

    /**
     * Custom painting for settings title
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw title
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        String title = "Settings";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        int x = (Constants.MENU_PANEL_WIDTH - titleWidth) / 2;

        g2d.drawString(title, x, 72);
    }
}