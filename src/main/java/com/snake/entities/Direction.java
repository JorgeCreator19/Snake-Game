package com.snake.entities;

public enum Direction {
    // Directions with their respective delta X and delta Y values
    UP(0, -1),
    DOWN(0, +1),
    LEFT(-1, 0),
    RIGHT(+1, 0);

    private final int dx;
    private final int dy;

    // Constructor
    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // GETTERS
    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    // Method to get the opposite direction
    public Direction getOpposite() {
        switch (this) {
            case UP:    return DOWN;
            case DOWN:  return UP;
            case LEFT:  return RIGHT;
            case RIGHT: return LEFT;
            default:    return this;
        }
    }
}