package com.snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.snake.entities.Direction;
import com.snake.entities.Food;
import com.snake.entities.Snake;
import com.snake.utils.Constants;
import com.snake.utils.SoundManager;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    
    // ===== GAME OBJECT =====
    private Snake snake;
    private Food food;
    private GameState gameState;
    private int score;
    private SoundManager soundManager;
    private GameFrame gameFrame;

    // ===== GAME LOOP =====
    private Timer gameTimer;

    // ===== COLORS =====
    private static final Color COLOR_BACKGROUND = new Color(30, 30,30);
    private static final Color COLOR_GRID = new Color(50, 50, 50);
    private static final Color COLOR_SNAKE_HEAD = new Color(0, 225, 0);
    private static final Color COLOR_SNAKE_BODY = new Color(0, 150, 0);
    private static final Color COLOR_FOOD = new Color(200, 50, 0);
    private static final Color COLOR_TEXT = Color.WHITE;

    // ===== CONSTRUCTOR =====
    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // Set panel size
        setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
        setBackground(COLOR_BACKGROUND);

        // Enable keyboard input
        setFocusable(true);
        addKeyListener(this);

        // Initialize game
        initGame();

        // Initialize sound manager
        soundManager = SoundManager.getInstance();

        // Start background music
        soundManager.playBackgroundLoop();

        // Start the game loop timer
        gameTimer = new Timer(Constants.GAME_SPEED, this);
        gameTimer.start();
    }

    /**
     * Initializes or resets the game state
     */
    private void initGame() {
        snake = new Snake();
        food = new Food();
        food.respawn(snake);
        gameState = GameState.PLAYING;
        score = 0;
    }

    /**
     * Called every time the Timer fires (every 150ms)
     * This IS the game loop
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.PLAYING) {
            updateGame();
        }
        repaint(); // Always repaint to show changes
    }

    /**
     * Updates all game logic for one frame
     */
    private void updateGame() {
        // 1. Move snake
        snake.move();

        // 2. Check collisions
        checkCollisions();

        // 3. Check if snake ate food
        checkFood();
    }

    /**
     * Checks if snake hit wall or itself
     */
    private void checkCollisions() {
        if (snake.hasCollideWithWall() || snake.hasCollideWithSelf()) {
            gameState = GameState.GAME_OVER;
            gameTimer.stop();
            soundManager.stopBackground();  // Stop music
            soundManager.playGameOver();    // Play game over sound
        }
    }

    /**
     * Checks if snake head is on food
     */
    private void checkFood() {
        if (snake.isHeadAt(food.getPosition())) {
            snake.grow();
            score += 10;
            food.respawn(snake);
            soundManager.playEat();  // Play eat sound
        }
    }

    // ===== RENDERING PANEL =====

    /**
     * Draws everything on screen
     * Called automatically when repaint() is called
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //  Draw game elements
        drawGrid(g2d);
        drawFood(g2d);
        drawSnake(g2d);
        drawScore(g2d);
        drawPosition(g2d);  

        // Draw overlay screens
        if (gameState == GameState.PAUSED) {
            drawPauseScreen(g2d);
        } else if (gameState == GameState.GAME_OVER) {
            drawGameOverScreen(g2d);
        }
    }

    /**
     * Draws the background grid
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(COLOR_GRID);

        // Vertical lines
        for (int x = 0; x <= Constants.GRID_WIDTH; x++) {
            int pixelX = x * Constants.CELL_SIZE;
            g2d.drawLine(pixelX, 0, pixelX, Constants.GAME_HEIGHT);
        }

        // Horizontal lines
        for (int y = 0; y <= Constants.GRID_HEIGHT; y++) {
            int pixelY = y * Constants.CELL_SIZE;
            g2d.drawLine(0, pixelY, Constants.GAME_WIDTH, pixelY);
        }
    }

    /**
     * Draws the snake
     */
    private void drawSnake(Graphics2D g2d) {
        List<Point> body = snake.getBody();

        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);

            // Head is brighter green
            if (i == 0) {
                g2d.setColor(COLOR_SNAKE_HEAD);
            } else {
                g2d.setColor(COLOR_SNAKE_BODY);
            }

            fillCell(g2d, segment.x, segment.y);
        }
    }

    /**
     * Draws the food
     */
    private void drawFood(Graphics2D g2d) {
        g2d.setColor(COLOR_FOOD);
        fillCell(g2d, food.getX(), food.getY());
    }

    /**
     * Draws the score at top of screen
     */
    private void drawScore(Graphics2D g2d) {
        g2d.setColor(COLOR_TEXT);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Score: " + score, 10, 20);
    }

    /**
     * Draws the actual snake position at top of screen below the score
     */
    private void drawPosition(Graphics2D g2d) {
        Point head = snake.getHead();
        g2d.setColor(COLOR_TEXT);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Position: " + head.x + ", " + head.y, 10, 40);
    }

    /**
     * Draws pause overlay 
     */
    private void drawPauseScreen(Graphics2D g2d) {
        // Dark overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Pause text
        g2d.setColor(COLOR_TEXT);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g2d, "GAME PAUSED", Constants.GAME_HEIGHT / 2 - 20);

        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(g2d, "Press P to continue", Constants.GAME_HEIGHT / 2 + 20);
    }

    /**
     * Draws game over overlay
     */
    private void drawGameOverScreen(Graphics2D g2d) {
        // Red overlay
        g2d.setColor(new Color(100, 0, 0, 150));
        g2d.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // Game over text
        g2d.setColor(new Color(200, 0, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g2d, "GAME OVER", Constants.GAME_HEIGHT / 2 - 40);

        // Final score
        g2d.setColor(COLOR_TEXT);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g2d, "Final Score: " + score, Constants.GAME_HEIGHT / 2);

        // Restart instruction
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCenteredString(g2d, "Press R to restart the game", Constants.GAME_HEIGHT / 2 + 40);
    }

    // ===== KEYBOARD INPUT =====

    /**
     * Called when a key is pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Arrow keys - only when playing
        if (gameState == GameState.PLAYING) {
            switch (key) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    snake.setDirection(Direction.UP);
                    break;
                
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    snake.setDirection(Direction.DOWN);
                    break;
                
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    snake.setDirection(Direction.LEFT);
                    break;
                
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    snake.setDirection(Direction.RIGHT);
                    break;
            }
        }

        // P - Pause toggle
        if (key == KeyEvent.VK_P) {
            togglePause();
        }

        // R - Restart 
        if (key == KeyEvent.VK_R && gameState == GameState.GAME_OVER) {
            restartGame();
        }

        // ESC - Back to menu
        if (key == KeyEvent.VK_ESCAPE) {
            gameTimer.stop();
            soundManager.stopBackground();
            gameFrame.showMenu();
        }
    }

    /**
     * Toggles between PLAYING and PAUSED
     */
    private void togglePause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            gameTimer.stop();
            soundManager.stopBackground();  // Stop music when paused
            repaint();
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
            gameTimer.start();
            soundManager.playBackgroundLoop();  // Resume music
        }
    }

    /**
     * Restarts the game
     */
    private void restartGame() {
        initGame();
        gameTimer.start();
        soundManager.playBackgroundLoop();  // Restart music
    }

    // ===== HELPER METHODS =====

    /**
     * Fills a cell at grid coordinates
     */
    private void fillCell(Graphics2D g2d, int gridX, int gridY) {
        int pixelX = gridX * Constants.CELL_SIZE;
        int pixelY = gridY * Constants.CELL_SIZE;
        g2d.fillRect(pixelX + 1, pixelY + 1, Constants.CELL_SIZE - 2, Constants.CELL_SIZE - 2);
    }

    /**
     * Draws text centered horizontally
     */
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (Constants.GAME_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    /**
     * This is required by KeyListener to work the class
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}