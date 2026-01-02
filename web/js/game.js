// ===================================================================
//                 SNAKE GAME - JavaScript Version
// ===================================================================

// ===== CONSTANTS =====
// Same as Constants.java - all game settings in one place+
const CELL_SIZE = 25;
const GRID_WIDTH = 20;
const GAME_WIDTH = GRID_WIDTH * CELL_SIZE;   // 500px
const GAME_HEIGHT = GRID_HEIGHT * CELL_SIZE; // 500px
const GAME_SPEED = 150; // milliseconds between updates
const INITIAL_SNAKE_LENGTH = 3;

// ===== COLORS =====
const COLORS = {
    background: '#1e1e1e',
    grid: '#333333',
    snakeHead: '#00c800',
    snakeBody: '#009600',
    food: '#c83232'
};

// ===== DIRECTIONS =====
// Same as Direction.java enum
const Direction = {
    UP: { dx: 0, dy: -1 },
    DOWN: { dx: 0, dy: 1 },
    LEFT: { dx: -1, dy: 0 },
    RIGHT: { dx: 1, dy: 0 }
};

// Helper function to check if directions are opposite
function isOpposite(dir1, dir2) {
    return dir1.dx === -dir2.dx && dir1.dy === -dir2.dy;
}

// ===== GAME STATES =====
// Same as GameState.java enum
const GameState = {
    MENU: 'menu',
    PLAYING: 'playing',
    PAUSED: 'paused',
    GAME_OVER: 'gameover',
    SETTINGS: 'settings',
    CONTROLS: 'controls'
};

// ===== GLOBAL VARIABLES =====
let canvas, ctx;    // Canvas and context for drawing
let snake, food;    // Game objects
let gameState;      // Current state
let score;          // Player score
let gameLoop;       // Interval ID for game loop
let soundManager;   // Sound manager instance