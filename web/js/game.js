// ===================================================================
// SNAKE GAME - JavaScript Version
// ===================================================================

console.log('game.js loaded!');

// ===== CONSTANTS =====
const CELL_SIZE = 25;
const GRID_WIDTH = 20;
const GRID_HEIGHT = 20;
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
const GameState = {
    MENU: 'menu',
    PLAYING: 'playing',
    PAUSED: 'paused',
    GAME_OVER: 'gameover',
    SETTINGS: 'settings',
    CONTROLS: 'controls'
};

// ===== GLOBAL VARIABLES =====
let canvas, ctx;
let snake, food;
let gameState;
let score;
let gameLoop;
let soundManager;

// ===================================================================
// SOUND MANAGER CLASS
// ===================================================================
class SoundManager {
    constructor() {
        this.musicVolume = 0.8;
        this.sfxVolume = 0.8;
        this.muted = false;
        
        // Try to load sounds (won't crash if files missing)
        this.eatSound = this.createAudio('sounds/eat.wav');
        this.gameOverSound = this.createAudio('sounds/gameover.wav');
        this.backgroundMusic = this.createAudio('sounds/background.mp3');
        
        if (this.backgroundMusic) {
            this.backgroundMusic.loop = true;
        }
    }
    
    // Safely create audio element
    createAudio(src) {
        try {
            const audio = new Audio(src);
            audio.volume = this.sfxVolume;
            return audio;
        } catch (e) {
            console.log('Could not load audio:', src);
            return null;
        }
    }
    
    playEat() {
        if (!this.muted && this.eatSound) {
            this.eatSound.volume = this.sfxVolume;
            this.eatSound.currentTime = 0;
            this.eatSound.play().catch(e => {});
        }
    }
    
    playGameOver() {
        if (!this.muted && this.gameOverSound) {
            this.gameOverSound.volume = this.sfxVolume;
            this.gameOverSound.currentTime = 0;
            this.gameOverSound.play().catch(e => {});
        }
    }
    
    playBackgroundLoop() {
        if (!this.muted && this.backgroundMusic) {
            this.backgroundMusic.volume = this.musicVolume;
            this.backgroundMusic.play().catch(e => {});
        }
    }
    
    stopBackground() {
        if (this.backgroundMusic) {
            this.backgroundMusic.pause();
            this.backgroundMusic.currentTime = 0;
        }
    }
    
    pauseBackground() {
        if (this.backgroundMusic) {
            this.backgroundMusic.pause();
        }
    }
    
    resumeBackground() {
        if (!this.muted && this.backgroundMusic) {
            this.backgroundMusic.play().catch(e => {});
        }
    }
    
    setMusicVolume(volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (this.backgroundMusic) {
            this.backgroundMusic.volume = this.musicVolume;
        }
    }
    
    setSfxVolume(volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }
    
    setMuted(muted) {
        this.muted = muted;
        if (muted) {
            this.stopBackground();
        }
    }
}

// ===================================================================
// SNAKE CLASS
// ===================================================================
class Snake {
    constructor() {
        this.body = [];
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.shouldGrow = false;
        
        this.initializeBody();
    }
    
    initializeBody() {
        const startX = Math.floor(GRID_WIDTH / 2);
        const startY = Math.floor(GRID_HEIGHT / 2);
        
        for (let i = 0; i < INITIAL_SNAKE_LENGTH; i++) {
            this.body.push({ x: startX - i, y: startY });
        }
    }
    
    move() {
        this.direction = this.nextDirection;
        
        const head = this.body[0];
        const newHead = {
            x: head.x + this.direction.dx,
            y: head.y + this.direction.dy
        };
        
        this.body.unshift(newHead);
        
        if (this.shouldGrow) {
            this.shouldGrow = false;
        } else {
            this.body.pop();
        }
    }
    
    setDirection(newDirection) {
        if (!isOpposite(this.direction, newDirection)) {
            this.nextDirection = newDirection;
        }
    }
    
    grow() {
        this.shouldGrow = true;
    }
    
    getHead() {
        return this.body[0];
    }
    
    hasCollidedWithWall() {
        const head = this.getHead();
        return head.x < 0 || head.x >= GRID_WIDTH ||
               head.y < 0 || head.y >= GRID_HEIGHT;
    }
    
    hasCollidedWithSelf() {
        const head = this.getHead();
        
        for (let i = 1; i < this.body.length; i++) {
            if (head.x === this.body[i].x && head.y === this.body[i].y) {
                return true;
            }
        }
        return false;
    }
    
    isHeadAt(position) {
        const head = this.getHead();
        return head.x === position.x && head.y === position.y;
    }
    
    occupies(position) {
        return this.body.some(segment => 
            segment.x === position.x && segment.y === position.y
        );
    }
}

// ===================================================================
// FOOD CLASS
// ===================================================================
class Food {
    constructor() {
        this.position = { x: 0, y: 0 };
        this.spawnRandom();
    }
    
    spawnRandom() {
        this.position.x = Math.floor(Math.random() * GRID_WIDTH);
        this.position.y = Math.floor(Math.random() * GRID_HEIGHT);
    }
    
    respawn(snake) {
        do {
            this.spawnRandom();
        } while (snake.occupies(this.position));
    }
    
    getPosition() {
        return this.position;
    }
}

// ===================================================================
// RENDERING FUNCTIONS
// ===================================================================

function render() {
    ctx.fillStyle = COLORS.background;
    ctx.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    
    drawGrid();
    drawFood();
    drawSnake();
}

function drawGrid() {
    ctx.strokeStyle = COLORS.grid;
    ctx.lineWidth = 1;
    
    for (let x = 0; x <= GRID_WIDTH; x++) {
        ctx.beginPath();
        ctx.moveTo(x * CELL_SIZE, 0);
        ctx.lineTo(x * CELL_SIZE, GAME_HEIGHT);
        ctx.stroke();
    }
    
    for (let y = 0; y <= GRID_HEIGHT; y++) {
        ctx.beginPath();
        ctx.moveTo(0, y * CELL_SIZE);
        ctx.lineTo(GAME_WIDTH, y * CELL_SIZE);
        ctx.stroke();
    }
}

function drawSnake() {
    snake.body.forEach((segment, index) => {
        ctx.fillStyle = index === 0 ? COLORS.snakeHead : COLORS.snakeBody;
        
        ctx.fillRect(
            segment.x * CELL_SIZE + 1,
            segment.y * CELL_SIZE + 1,
            CELL_SIZE - 2,
            CELL_SIZE - 2
        );
    });
}

function drawFood() {
    ctx.fillStyle = COLORS.food;
    ctx.fillRect(
        food.position.x * CELL_SIZE + 1,
        food.position.y * CELL_SIZE + 1,
        CELL_SIZE - 2,
        CELL_SIZE - 2
    );
}

function updateScoreDisplay() {
    document.getElementById('score-display').textContent = `Score: ${score}`;
}

function updatePositionDisplay() {
    const head = snake.getHead();
    document.getElementById('position-display').textContent = `Position: (${head.x}, ${head.y})`;
}

// ===================================================================
// GAME LOGIC FUNCTIONS
// ===================================================================

function updateGame() {
    if (gameState !== GameState.PLAYING) return;
    
    snake.move();
    checkCollisions();
    checkFood();
    render();
    updatePositionDisplay();
}

function checkCollisions() {
    if (snake.hasCollidedWithWall() || snake.hasCollidedWithSelf()) {
        gameOver();
    }
}

function checkFood() {
    if (snake.isHeadAt(food.getPosition())) {
        snake.grow();
        score += 10;
        updateScoreDisplay();
        food.respawn(snake);
        soundManager.playEat();
    }
}

function gameOver() {
    gameState = GameState.GAME_OVER;
    clearInterval(gameLoop);
    soundManager.stopBackground();
    soundManager.playGameOver();
    
    document.getElementById('final-score').textContent = `Final Score: ${score}`;
    document.getElementById('gameover-overlay').classList.remove('hidden');
}

function startGame() {
    snake = new Snake();
    food = new Food();
    food.respawn(snake);
    score = 0;
    gameState = GameState.PLAYING;
    
    updateScoreDisplay();
    updatePositionDisplay();
    
    document.getElementById('pause-overlay').classList.add('hidden');
    document.getElementById('gameover-overlay').classList.add('hidden');
    
    clearInterval(gameLoop);
    gameLoop = setInterval(updateGame, GAME_SPEED);
    
    soundManager.playBackgroundLoop();
    
    render();
}

function togglePause() {
    if (gameState === GameState.PLAYING) {
        gameState = GameState.PAUSED;
        clearInterval(gameLoop);
        soundManager.pauseBackground();
        document.getElementById('pause-overlay').classList.remove('hidden');
    } else if (gameState === GameState.PAUSED) {
        gameState = GameState.PLAYING;
        gameLoop = setInterval(updateGame, GAME_SPEED);
        soundManager.resumeBackground();
        document.getElementById('pause-overlay').classList.add('hidden');
    }
}

function restartGame() {
    document.getElementById('gameover-overlay').classList.add('hidden');
    startGame();
}

function goToMenu() {
    gameState = GameState.MENU;
    clearInterval(gameLoop);
    soundManager.stopBackground();
    showScreen('menu-screen');
}

// ===================================================================
// SCREEN MANAGEMENT
// ===================================================================

function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.add('hidden');
    });
    
    document.getElementById(screenId).classList.remove('hidden');
}

function showGameScreen() {
    showScreen('game-screen');
    startGame();
}

function showSettingsScreen() {
    showScreen('settings-screen');
}

function showControlsScreen() {
    showScreen('controls-screen');
}

function showMenuScreen() {
    showScreen('menu-screen');
}

// ===================================================================
// KEYBOARD INPUT
// ===================================================================

function handleKeyDown(event) {
    const key = event.key;
    
    if (gameState === GameState.PLAYING) {
        switch (key) {
            case 'ArrowUp':
            case 'w':
            case 'W':
                snake.setDirection(Direction.UP);
                event.preventDefault();
                break;
                
            case 'ArrowDown':
            case 's':
            case 'S':
                snake.setDirection(Direction.DOWN);
                event.preventDefault();
                break;
                
            case 'ArrowLeft':
            case 'a':
            case 'A':
                snake.setDirection(Direction.LEFT);
                event.preventDefault();
                break;
                
            case 'ArrowRight':
            case 'd':
            case 'D':
                snake.setDirection(Direction.RIGHT);
                event.preventDefault();
                break;
        }
    }
    
    if (key === 'p' || key === 'P') {
        if (gameState === GameState.PLAYING || gameState === GameState.PAUSED) {
            togglePause();
        }
    }
    
    if ((key === 'r' || key === 'R') && gameState === GameState.GAME_OVER) {
        restartGame();
    }
    
    if (key === 'Escape') {
        if (gameState === GameState.PLAYING || 
            gameState === GameState.PAUSED || 
            gameState === GameState.GAME_OVER) {
            goToMenu();
        }
    }
}

// ===================================================================
// SETTINGS FUNCTIONS
// ===================================================================

function setupSettings() {
    const musicSlider = document.getElementById('music-volume');
    const musicValue = document.getElementById('music-value');
    
    musicSlider.addEventListener('input', () => {
        const volume = musicSlider.value / 100;
        soundManager.setMusicVolume(volume);
        musicValue.textContent = `${musicSlider.value}%`;
    });
    
    const sfxSlider = document.getElementById('sfx-volume');
    const sfxValue = document.getElementById('sfx-value');
    
    sfxSlider.addEventListener('input', () => {
        const volume = sfxSlider.value / 100;
        soundManager.setSfxVolume(volume);
        sfxValue.textContent = `${sfxSlider.value}%`;
    });
    
    const muteCheckbox = document.getElementById('mute-checkbox');
    
    muteCheckbox.addEventListener('change', () => {
        soundManager.setMuted(muteCheckbox.checked);
    });
}

// ===================================================================
// INITIALIZATION
// ===================================================================

function init() {
    console.log('init() called');
    
    // Get canvas and context
    canvas = document.getElementById('game-canvas');
    
    if (!canvas) {
        console.error('Canvas not found!');
        return;
    }
    
    ctx = canvas.getContext('2d');
    
    // Set canvas size
    canvas.width = GAME_WIDTH;
    canvas.height = GAME_HEIGHT;
    
    console.log('Canvas size:', canvas.width, 'x', canvas.height);
    
    // Initialize sound manager
    soundManager = new SoundManager();
    
    // Setup settings sliders
    setupSettings();
    
    // Setup button click handlers
    setupButtons();
    
    // Add keyboard listener
    document.addEventListener('keydown', handleKeyDown);
    
    // Start at menu
    gameState = GameState.MENU;
    showScreen('menu-screen');
    
    console.log('Snake Game initialized!');
}

function setupButtons() {
    // Menu buttons
    document.getElementById('play-btn').addEventListener('click', showGameScreen);
    document.getElementById('settings-btn').addEventListener('click', showSettingsScreen);
    document.getElementById('controls-btn').addEventListener('click', showControlsScreen);
    
    // Settings back button
    document.getElementById('settings-back-btn').addEventListener('click', showMenuScreen);
    
    // Controls back button
    document.getElementById('controls-back-btn').addEventListener('click', showSettingsScreen);
    
    // Game over buttons
    document.getElementById('restart-btn').addEventListener('click', restartGame);
    document.getElementById('menu-btn').addEventListener('click', goToMenu);
}

// Start when page loads
window.addEventListener('load', init);