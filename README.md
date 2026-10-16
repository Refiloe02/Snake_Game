# Snake Game

A classic Snake game built in Java with Swing. Steer the snake around the board, eat the red food to grow, and avoid the walls and your own tail.

## Features

- Arrow-key controls
- Snake grows each time it eats food
- Food spawns at a random tile
- Live score (the length of the snake's body)
- Game over when the snake hits a wall or itself
- Unit tests with JUnit 5

## Requirements

- Java 17 or newer
- Maven 3.8 or newer

Check your versions:

```bash
java -version
mvn -version
```

## Getting Started

Clone the repository and move into it:

```bash
git clone git@github.com:Refiloe02/Snake_Game.git
cd Snake_Game
```

Compile and run:

```bash
mvn compile
java -cp target/classes App
```

You can also run `App.java` directly from VS Code or IntelliJ.

## How to Play

| Key | Action |
|-----|--------|
| Up arrow | Move up |
| Down arrow | Move down |
| Left arrow | Move left |
| Right arrow | Move right |

- The snake stands still until you press a key.
- The snake cannot reverse straight back into itself (for example, you can't go left while moving right).
- Eating food adds one segment and increases your score by 1.
- The game ends if the snake leaves the board or runs into its own body. The score area then shows **Game Over** with your final score.

## Running the Tests

```bash
mvn clean test
```

The tests live in `src/test/java/SnakeTest.java` and cover:

- Starting position and initial state
- Movement in all four directions
- Keyboard input, including the rule against reversing
- Eating food, growing and the body following the head
- Game over from each wall and from hitting the snake's own body
- The game loop stopping after game over

If the build reports `Tests run: 0`, check that `maven-surefire-plugin` (version 3.x) is declared in `pom.xml`. Older Surefire versions don't run JUnit 5 tests.

## Project Structure

```
Snake_Game/
├── pom.xml
└── src/
    ├── main/java/
    │   ├── App.java         # Creates the window and starts the game
    │   └── SnakeGame.java   # Game logic, drawing and keyboard handling
    └── test/java/
        └── SnakeTest.java   # JUnit 5 tests
```

## How It Works

- The board is 600 x 600 pixels, divided into 25 px tiles.
- `SnakeGame` extends `JPanel` and implements `ActionListener` and `KeyListener`.
- A Swing `Timer` fires every 100 ms. Each tick calls `move()` to update the game, then `repaint()` to redraw it.
- Each tick, `move()` checks for food, shifts every body segment into the position of the one in front of it, moves the head, then checks for collisions.

## Known Issues

- The right and bottom wall checks use `>` instead of `>=`, so the snake can move one tile past the visible edge before the game ends.
- Food can spawn on top of the snake's body.
- There is no restart option after game over.

## Built With

- Java 21
- Swing / AWT
- Maven
- JUnit 5