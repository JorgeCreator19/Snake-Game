# 🐍 Snake Game

A classic Snake game built with Java and Java2D graphics.

## Features

- Classic snake gameplay
- Main menu with Play, Settings, and Exit
- Sound effects and background music
- Volume controls
- Pause functionality
- Game controls screen

## Controls

| Key | Action |
|-----|--------|
| W / ↑ | Move Up |
| S / ↓ | Move Down |
| A / ← | Move Left |
| D / → | Move Right |
| P | Pause |
| R | Restart (when game over) |
| ESC | Back to Menu |

## How to Run

### With Maven
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.snake.Main"
```

### Without Maven
```bash
cd src/main/java
javac com/snake/Main.java com/snake/**/*.java
java com.snake.Main
```

## Built With

- Java 17
- Java2D Graphics
- Swing GUI
- Maven

## Author

JorgeCreator19
