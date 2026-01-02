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

// ===================================================================
//                        SOUND MANAGER CLASS
//                      Same as SoundManager.java
// ===================================================================
class SoundManager {
    constructor() {
        // Create audio elements
        this.eatSound = new Audio('sounds/eat.wav');
        this.gameOverSound = new Audio('sounds/gameover.wav');
        this.backgroundMusic = new Audio('sounds/background.mp3');
        
        // Settings
        this.musicVolume = 0.8;
        this.sfxVolume = 0.8;
        this.muted = false;
        
        // Background music should loop
        this.backgroundMusic.loop = true;
    }

    // Play eat sound
    playEat() {
        if (!this.muted) {
            this.eatSound.volume = this.sfxVolume;
            this.eatSound.currentTime = 0; // Rewind to start
            this.eatSound.play().catch(e => console.log('Audio play failed:', e))
        }
    }

    // Play game over sound
    playGameOver() {
        if (!this.muted) {
            this.gameOverSound.volume = this.sfxVolume;
            this.gameOverSound.currentTime = 0;
            this.gameOverSound.play().catch(e => console.log('Audio play failed:', e))
        }
    }

    // Start background music
    playBackgroundLoop() {
        if (!this.muted) {
            this.backgroundMusic.volume = this.musicVolume;
            this.backgroundMusic.play().catch(e => console.log('Audio play failed:', e));
        }
    }

    // Stop background music
    stopBackground() {
        this.backgroundMusic.pause();
        this.backgroundMusic.currentTime = 0;
    }

    // Pause background music (without rewinding)
    pauseBackground() {
        this.backgroundMusic.pause();
    }

    // Resume background music
    resumeBackground() {
        if (!this.muted) {
            this.backgroundMusic.play().catch(e => console.log('Audio play failed:', e))
        }
    }

    // Set music volume (0.0 to 1.0)
    setMusicVolume(volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        this.backgroundMusic.volume = this.musicVolume;
    }

    // Set music volume (0.0 to 1.0)
    setSfxVolume(volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }

    // Set muted state
    setMuted(muted) {
        this.muted = muted;
        if (muted) {
            this.stopBackground();
        }
    }
}

// ===================================================================
//                              SNAKE CLASS
//                           Same as Snake.java
// ===================================================================
class Snake {
     constructor() {
        this.body = [];              // Array of {x, y} positions
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.shouldGrow = false;
        
        this.initializeBody();
    }

    // Create snake at center of grid
    initializeBody() {
        const startX = Math.floor(GRID_WIDTH / 2);
        const startY = Math.floor(GRID_HEIGHT / 2);
        
        // Add segments: head first, then body going LEFT
        for (let i = 0; i < INITIAL_SNAKE_LENGTH; i++) {
            this.body.push({ x: startX - i, y: startY });
        }
    }

    // Move snake one cell
    move() {
        // Apply buffered direction
        this.direction = this.nextDirection;

        // Get current head
        const head = this.body[0];

        // Calculate new head position
        const newHead = {
            x: head.x + this.direction.dx,
            y: head.y + this.direction.dy
        };

        // Add new head at beginning
        this.body.unshift(newHead);

        // Remove tail (unless growing)
        if (this.shouldGrow) {
            this.shouldGrow = false;
        } else {
            this.body.pop();
        }
    }

    // Set direction (prevent reversing)
    setDirection(newDirection) {
        if (!isOpposite(this.direction, newDirection)) {
            this.nextDirection = newDirection;
        }
    }

    // Grow on next move
    grow() {
        this.shouldGrow = true;
    }
    
    // Get head position
    getHead() {
        return this.body[0];
    }
    
    // Check if head hit wall
    hasCollidedWithWall() {
        const head = this.getHead();
        return head.x < 0 || head.x >= GRID_WIDTH ||
               head.y < 0 || head.y >= GRID_HEIGHT;
    }

    // Check if head hit body
    hasCollidedWithSelf() {
        const head = this.getHead();
        
        // Check against all body segments except head (index 0)
        for (let i = 1; i < this.body.length; i++) {
            if (head.x === this.body[i].x && head.y === this.body[i].y) {
                return true;
            }
        }
        return false;
    }

     // Check if head is at position
    isHeadAt(position) {
        const head = this.getHead();
        return head.x === position.x && head.y === position.y;
    }
    
    // Check if any segment occupies position
    occupies(position) {
        return this.body.some(segment => 
            segment.x === position.x && segment.y === position.y
        );
    }
}

// ===================================================================
//                              FOOD CLASS
//                          Same as Food.java
// ===================================================================
class Food {
    constructor() {
        this.position = { x: 0, y: 0 };
        this.spawnRandom();
    }

    // Spawn at random position
    spawnRandom() {
        this.position.x = Math.floor(Math.random() * GRID_WIDTH);
        this.position.y = Math.floor(Math.random() * GRID_HEIGHT);
    }

    // Spawn avoiding snake
    respawn(snake) {
        do {
            this.spawnRandom();
        } while (snake.occupies(this.position));
    }

    getPosition() {
        return this.position;
    }
}