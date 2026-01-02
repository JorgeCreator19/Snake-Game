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
// Same as SoundManager.java
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