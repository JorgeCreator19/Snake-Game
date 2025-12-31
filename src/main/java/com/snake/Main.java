package com.snake;

import javax.swing.SwingUtilities;

import com.snake.game.GameFrame;

public class Main {

    public static void main(String[] args) {
        // Run on Swing's Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the game window
                new GameFrame();
            }
        });
    }
}