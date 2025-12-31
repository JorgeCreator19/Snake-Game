package com.snake.game;

public enum GameState {
    PLAYING, // Game is running, snake moves
    PAUSED, // Game is frozen, waiting to resume
    GAME_OVER // Snake died, waiting to restart
}