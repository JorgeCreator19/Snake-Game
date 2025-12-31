package com.snake.utils;

public class Constants {
    // Grid dimensions settings
    public static final int CELL_SIZE = 25;
    public static final int GRID_WIDTH = 20;
    public static final int GRID_HEIGHT = 20;
    public static final int PANEL_WIDTH = 30;
    public static final int PANEL_HEIGHT = 25;

    // Game settings
    public static final int GAME_SPEED = 150;
    public static final int INITIAL_SNAKE_LENGTH = 3;

    // Calculated values with the constants
    public static final int GAME_WIDTH = GRID_WIDTH * CELL_SIZE;
    public static final int GAME_HEIGHT = GRID_WIDTH * CELL_SIZE;
    public static final int MENU_PANEL_WIDTH = PANEL_WIDTH * CELL_SIZE;
    public static final int MENU_PANEL_HEIGHT = PANEL_HEIGHT * CELL_SIZE;

    // Private constructor
    private Constants() {
        // Empty to prevents instantiation
    }   
}