package model;

import model.pieces.Tetromino;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TetrominoTest {
    protected Tetromino t;

    // This game contains three "I" pieces that are dropped into very specific places.
    // The first "I" piece is dropped straight down onto the floor, in the same horizontal position
    // that it spawned in.
    // The second "I" piece is dropped on top of the leftmost tile of the first "I" piece, and is rotated
    // so that it stands upright.
    // The third "I" piece is dropped on top of the right tile of the first "I" piece, and is rotated
    // so that it stands upright.
    // This setup creates an open box on the board that can be used to test the rotate, moveLeft,
    // moveRight, and moveDown methods.
    protected Game testGame;

    // This seed has been tested and is guaranteed to generate three "I" pieces in a row at the beginning
    // of the game.
    private static final int TEST_GAME_SEED = 768;

    // We want this @BeforeEach method to run before the @BeforeEach methods in the subclasses of
    // TetrominoTest. Fortunately, https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html
    // tells us that this behaviour will happen.
    @BeforeEach
    public void setUpTestGame() {
        testGame = new Game();
        testGame.startNewGame(TEST_GAME_SEED);

        // Drop the first "I" piece to the floor
        for (int i = 0; i < Game.HEIGHT; i++) {
            testGame.update();
        }

        // Drop the second "I" piece to the appropriate place
        testGame.update();
        testGame.getActiveTetromino().rotate();
        testGame.getActiveTetromino().moveLeft();
        testGame.getActiveTetromino().moveLeft();
        for (int i = 0; i < Game.HEIGHT - 2; i++) {
            testGame.update();
        }

        // Drop the third "I" piece to the appropriate place
        testGame.update();
        testGame.getActiveTetromino().rotate();
        testGame.getActiveTetromino().moveRight();
        testGame.getActiveTetromino().moveRight();
        for (int i = 0; i < Game.HEIGHT - 2; i++) {
            testGame.update();
        }

        // Ensure that we only have 12 tiles on the board.
        int numTiles = 0;
        for (List<Boolean> column : testGame.getBoard()) {
            for (boolean cellOccupied : column) {
                if (cellOccupied) {
                    numTiles++;
                }
            }
        }

        assertEquals(12, numTiles);
    }

    @Test
    public void testMoveLeftNotBoundary() {
        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveLeft();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x + 1, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveLeftWallBoundary() {
        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveLeft();
        }

        // By now, t is guaranteed to be at the left wall

        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveLeft();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    // Test that moving leftwards into another tile does nothing
    @Test
    public abstract void testMoveLeftSpaceOccupiedByTile();

    @Test
    public void testMoveRightNotBoundary() {
        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveRight();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x - 1, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveRightWallBoundary() {
        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveRight();
        }

        // By now, t is guaranteed to be at the right wall

        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveRight();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    // Test that moving rightwards into another tile does nothing
    @Test
    public abstract void testMoveRightSpaceOccupiedByTile();

    @Test
    public void testMoveDownNotBoundary() {
        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveDown();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y - 1)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveDownFloorBoundary() {
        for (int i = 0; i < Game.HEIGHT; i++) {
            t.moveDown();
        }

        // By now, t is guaranteed to be at the floor

        Set<Point> oldTileLocations = getTileLocationsCopy(t);
        t.moveDown();

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    // Test that moving downwards into another tile does nothing
    @Test
    public abstract void testMoveDownSpaceOccupiedByTile();

    // EFFECTS: returns a copy of tetromino.getTileLocations()
    private Set<Point> getTileLocationsCopy(Tetromino tetromino) {
        Set<Point> tileLocations = new HashSet<Point>();
        for (Point location : tetromino.getTileLocations()) {
            tileLocations.add(new Point(location));
        }
        return tileLocations;
    }
}
