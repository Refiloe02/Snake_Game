import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnakeTest {

    // ---------- helpers ----------

    // Creates a game with the timer stopped (so it can't move the snake mid-test)
    // and the food moved out of the way (so random placement can't affect results).
    private SnakeGame newGame() throws Exception {
        SnakeGame game = new SnakeGame(600, 600);
        game.gameLoop.stop();
        moveFoodAway(game);
        return game;
    }

    private void moveFoodAway(SnakeGame game) throws Exception {
        setCoordinate(game.food, "x", 20);
        setCoordinate(game.food, "y", 20);
    }

    // Tile is a private class, so read and write its fields with reflection.
    private int getCoordinate(Object tile, String fieldName) throws Exception {
        Field field = tile.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(tile);
    }

    private void setCoordinate(Object tile, String fieldName, int value) throws Exception {
        Field field = tile.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(tile, value);
    }

    private int headX(SnakeGame game) throws Exception {
        return getCoordinate(game.snakeHead, "x");
    }

    private int headY(SnakeGame game) throws Exception {
        return getCoordinate(game.snakeHead, "y");
    }

    private int bodyX(SnakeGame game, int index) throws Exception {
        return getCoordinate(game.snakeBody.get(index), "x");
    }

    private int bodyY(SnakeGame game, int index) throws Exception {
        return getCoordinate(game.snakeBody.get(index), "y");
    }

    // Builds a Tile with reflection and adds it to the snake's body.
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addBodyTile(SnakeGame game, int x, int y) throws Exception {
        Constructor<?> constructor = game.snakeHead.getClass()
                .getDeclaredConstructor(SnakeGame.class, int.class, int.class);
        constructor.setAccessible(true);
        Object tile = constructor.newInstance(game, x, y);
        ((List) game.snakeBody).add(tile);
    }

    private void press(SnakeGame game, int keyCode) {
        game.keyPressed(new KeyEvent(game, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED));
    }

    private void moveTimes(SnakeGame game, int times) {
        for (int i = 0; i < times; i++) {
            game.move();
        }
    }

    // ---------- starting state ----------

    @Test
    void snakeStartsAtExpectedPosition() throws Exception {
        SnakeGame game = newGame();

        assertEquals(5, headX(game));
        assertEquals(5, headY(game));
    }

    @Test
    void snakeStartsWithNoBodyAndNoVelocity() throws Exception {
        SnakeGame game = newGame();

        assertEquals(0, game.snakeBody.size());
        assertEquals(0, game.velocityX);
        assertEquals(0, game.velocityY);
        assertFalse(game.gameOver);
    }

    @Test
    void snakeStaysStillWithZeroVelocity() throws Exception {
        SnakeGame game = newGame();

        game.move();

        assertEquals(5, headX(game));
        assertEquals(5, headY(game));
    }

    // ---------- movement ----------

    @Test
    void snakeMovesDown() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = 1;

        game.move();

        assertEquals(5, headX(game));
        assertEquals(6, headY(game));
    }

    @Test
    void snakeMovesUp() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = -1;

        game.move();

        assertEquals(5, headX(game));
        assertEquals(4, headY(game));
    }

    @Test
    void snakeMovesRight() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();

        assertEquals(6, headX(game));
        assertEquals(5, headY(game));
    }

    @Test
    void snakeMovesLeft() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = -1;
        game.velocityY = 0;

        game.move();

        assertEquals(4, headX(game));
        assertEquals(5, headY(game));
    }

    // ---------- keyboard input ----------

    @Test
    void arrowKeysSetTheDirectionWhenStanding() throws Exception {
        SnakeGame game = newGame();

        press(game, KeyEvent.VK_UP);
        assertEquals(0, game.velocityX);
        assertEquals(-1, game.velocityY);

        game = newGame();
        press(game, KeyEvent.VK_DOWN);
        assertEquals(0, game.velocityX);
        assertEquals(1, game.velocityY);

        game = newGame();
        press(game, KeyEvent.VK_LEFT);
        assertEquals(-1, game.velocityX);
        assertEquals(0, game.velocityY);

        game = newGame();
        press(game, KeyEvent.VK_RIGHT);
        assertEquals(1, game.velocityX);
        assertEquals(0, game.velocityY);
    }

    @Test
    void snakeCanTurnPerpendicular() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 1;
        game.velocityY = 0;

        press(game, KeyEvent.VK_UP);

        assertEquals(0, game.velocityX);
        assertEquals(-1, game.velocityY);
    }

    @Test
    void snakeCannotReverseFromMovingRightToMovingLeft() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 1;
        game.velocityY = 0;

        press(game, KeyEvent.VK_LEFT);

        assertEquals(1, game.velocityX);
        assertEquals(0, game.velocityY);
    }

    @Test
    void snakeCannotReverseFromMovingLeftToMovingRight() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = -1;
        game.velocityY = 0;

        press(game, KeyEvent.VK_RIGHT);

        assertEquals(-1, game.velocityX);
        assertEquals(0, game.velocityY);
    }

    @Test
    void snakeCannotReverseFromMovingDownToMovingUp() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = 1;

        press(game, KeyEvent.VK_UP);

        assertEquals(0, game.velocityX);
        assertEquals(1, game.velocityY);
    }

    @Test
    void snakeCannotReverseFromMovingUpToMovingDown() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = -1;

        press(game, KeyEvent.VK_DOWN);

        assertEquals(0, game.velocityX);
        assertEquals(-1, game.velocityY);
    }

    // ---------- eating and growing ----------

    @Test
    void snakeDoesNotGrowWhenNotOnFood() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 1;
        game.velocityY = 0;

        moveTimes(game, 3);

        assertEquals(0, game.snakeBody.size());
    }

    @Test
    void snakeGrowsWhenItEatsFood() throws Exception {
        SnakeGame game = newGame();
        setCoordinate(game.food, "x", 5);   // food on the head's tile
        setCoordinate(game.food, "y", 5);
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();

        assertEquals(1, game.snakeBody.size());
        assertFalse(game.gameOver);
    }

    @Test
    void foodIsPlacedInsideTheBoardAfterEating() throws Exception {
        SnakeGame game = newGame();
        setCoordinate(game.food, "x", 5);
        setCoordinate(game.food, "y", 5);
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();

        int tiles = 600 / 25;
        assertTrue(getCoordinate(game.food, "x") >= 0 && getCoordinate(game.food, "x") < tiles);
        assertTrue(getCoordinate(game.food, "y") >= 0 && getCoordinate(game.food, "y") < tiles);
    }

    @Test
    void bodyFollowsTheHead() throws Exception {
        SnakeGame game = newGame();
        setCoordinate(game.food, "x", 5);
        setCoordinate(game.food, "y", 5);
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();                 // eats: head (6,5), body[0] (5,5)
        moveFoodAway(game);          // the food was re-placed randomly, so move it away
        game.move();                 // head (7,5), body[0] follows to (6,5)

        assertEquals(7, headX(game));
        assertEquals(5, headY(game));
        assertEquals(6, bodyX(game, 0));
        assertEquals(5, bodyY(game, 0));
    }

    // ---------- game over: walls ----------

    @Test
    void gameOverWhenSnakeHitsLeftWall() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = -1;
        game.velocityY = 0;

        moveTimes(game, 5);          // x goes 4, 3, 2, 1, 0
        assertFalse(game.gameOver);  // x = 0 is still on the board

        game.move();                 // x = -1
        assertTrue(game.gameOver);
    }

    @Test
    void gameOverWhenSnakeHitsTopWall() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = -1;

        moveTimes(game, 5);          // y = 0
        assertFalse(game.gameOver);

        game.move();                 // y = -1
        assertTrue(game.gameOver);
    }

    // With the current check (x * 25 > 600), x = 24 is still "alive" and
    // x = 25 is the first game over tile.
    @Test
    void gameOverWhenSnakeHitsRightWall() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 1;
        game.velocityY = 0;

        moveTimes(game, 19);         // x = 24
        assertFalse(game.gameOver);

        game.move();                 // x = 25
        assertTrue(game.gameOver);
    }

    @Test
    void gameOverWhenSnakeHitsBottomWall() throws Exception {
        SnakeGame game = newGame();
        game.velocityX = 0;
        game.velocityY = 1;

        moveTimes(game, 19);         // y = 24
        assertFalse(game.gameOver);

        game.move();                 // y = 25
        assertTrue(game.gameOver);
    }

    // ---------- game over: self collision ----------

    @Test
    void gameOverWhenSnakeHitsItself() throws Exception {
        SnakeGame game = newGame();
        addBodyTile(game, 6, 5);     // directly in front of the head
        addBodyTile(game, 7, 5);
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();                 // body shifts, head moves into (6,5)

        assertTrue(game.gameOver);
    }

    @Test
    void noGameOverWhenBodyIsNotInThePath() throws Exception {
        SnakeGame game = newGame();
        addBodyTile(game, 5, 6);     // below the head
        addBodyTile(game, 5, 7);
        game.velocityX = 1;
        game.velocityY = 0;

        game.move();

        assertFalse(game.gameOver);
    }

    // ---------- game loop ----------

    // move() only sets gameOver; actionPerformed() is what stops the timer.
    @Test
    void gameLoopStopsAfterGameOver() throws Exception {
        SnakeGame game = newGame();
        game.gameLoop.setInitialDelay(60_000);   // so the real timer can't fire during the test
        game.gameLoop.start();
        game.velocityX = -1;
        game.velocityY = 0;
        assertTrue(game.gameLoop.isRunning());

        for (int i = 0; i < 6; i++) {
            game.actionPerformed(null);          // 6th call goes off the left edge
        }

        assertTrue(game.gameOver);
        assertFalse(game.gameLoop.isRunning());
    }

    @Test
    void gameLoopKeepsRunningWhileGameIsNotOver() throws Exception {
        SnakeGame game = newGame();
        game.gameLoop.setInitialDelay(60_000);
        game.gameLoop.start();
        game.velocityX = 1;
        game.velocityY = 0;

        game.actionPerformed(null);

        assertFalse(game.gameOver);
        assertTrue(game.gameLoop.isRunning());
        game.gameLoop.stop();
    }
}