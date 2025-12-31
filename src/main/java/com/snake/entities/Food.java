package com.snake.entities;

import java.awt.Point;
import java.util.Random;

import com.snake.utils.Constants;

public class Food {
    
    private Point position;
    private Random random;

    // Constructor
    public Food() {
        random = new Random();
        position = new Point();
        spawnRandom();
    }

    /**
     * Spawns food at random position
     */
    public void spawnRandom() {
        int x = random.nextInt(Constants.GRID_WIDTH); // 0 to GRID_WIDTH-1
        int y = random.nextInt(Constants.GRID_HEIGHT); // 0 to GRID_HEIGHT-1
        position.setLocation(x, y);
    }

    /**
     * Spawns food avoiding the snake body
     * @param snake The snake to avoid
     */
    public void respawn(Snake snake) {
        do {
            spawnRandom();
        } while (snake.occupies(position)); // Keep trying while snake occupies position
    }

    // Getters
    public Point getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }
}