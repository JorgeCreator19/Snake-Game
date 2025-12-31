package com.snake.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.snake.utils.Constants;

public class Snake {
    
    private List<Point> body;
    private Direction direction;
    private Direction nextDirection;
    private boolean shouldGrow;

    // Constructor
    public Snake() {
        body = new ArrayList<Point>();
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        shouldGrow = false;
        initializeBody();
    }

    /**
     * Create snake at center of grid
     */
    private void initializeBody() {
        int startX = Constants.GRID_WIDTH / 2; // Center X
        int startY = Constants.GRID_HEIGHT / 2; // Center Y

        // Add segments: head first, then body going LEFT part
        for (int i = 0; i < Constants.INITIAL_SNAKE_LENGTH; i++) {
            body.add(new Point(startX - i, startY)); // Horizontal to the left
        }
    }

    /**
     * Moves the snake in the current direction
     */
    public void move() {
        direction = nextDirection; // Apply buffered direction - the same nextDirection

        // Set new X and Y to get Dx & Dy
        Point head = body.get(0);
        int newX = head.x + direction.getDx();
        int newY = head.y + direction.getDy();

        // Add new head position
        body.add(0, new Point(newX, newY));
        if (shouldGrow) {
            shouldGrow = false;
        } else {
            body.remove(body.size() - 1);
        }
    }

    /**
     * Set the new direction to the snake
     * @param newDirection The new direction to set
     */
    public void setDirection(Direction newDirection) {
        if (direction.getOpposite() != newDirection) {
            nextDirection = newDirection;
        }
    }

    /**
     * Snake grow the body when ate food
     */
    public void grow() {
        shouldGrow = true;
    }

    /**
     * Checks if the snake collides with the wall
     * @return true if collides, false otherwise
     */
    public boolean hasCollideWithWall() {
        Point head = getHead();
        return head.x < 0 || head.x >= Constants.GRID_WIDTH ||
                head.y < 0 || head.y >= Constants.GRID_HEIGHT;
    }

    /**
     * Checks if the snake collides with itself
     * @return true if collides, false otherwise
     */
    public boolean hasCollideWithSelf() {
        Point head = getHead();
        // Loop from index 1 to end (skip head at index 0)
        // If any segment equals head, return true
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the snake occupies a given position
     * @param position The position to check
     * @return true if occupies, false otherwise
     */
    public boolean occupies(Point position) {
        return body.contains(position);
    }

    /**
     * Checks if the snake's head is at a given position
     * @param position The position to check
     * @return true if head is at position, false otherwise
     */
    public boolean isHeadAt(Point position) {
        return getHead().equals(position);
    }

    // GETTERS
    public Point getHead() {
        return body.get(0);
    }

    public List<Point> getBody() {
        return body;
    }

    public int getLength() {
        return body.size();
    }
}