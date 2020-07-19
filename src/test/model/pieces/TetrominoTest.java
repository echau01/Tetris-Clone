package model.pieces;

import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TetrominoTest {
    protected static int TEST_GAME_WALL_HEIGHT = Game.HEIGHT / 2;

    protected Tetromino t;

    // Do not call update on this game!
    protected Game testGame;

    // We want this @BeforeEach method to run before the @BeforeEach methods in the subclasses of
    // TetrominoTest. Fortunately, https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html
    // tells us that this behaviour will happen.
    @BeforeEach
    public void setUpTestGame() {
        testGame = new Game();
        // Do NOT start the game. We do not want an active tetromino in the game.

        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);
        for (int i = Game.HEIGHT / 2; i < Game.HEIGHT; i++) {
            ArrayList<Boolean> row = riggedBoard.get(i);
            for (int j = 0; j < Game.WIDTH; j++) {
                if (j != approximateCenter - 2
                        && j != approximateCenter - 1
                        && j != approximateCenter
                        && j != approximateCenter + 1
                        && j != approximateCenter + 2) {
                    row.set(j, true);
                }
            }
        }

        testGame.setBoard(riggedBoard);
    }

    @Test
    public void testMoveLeftNotBoundary() {
        Set<Point> oldTileLocations = t.getTileLocations();
        assertTrue(t.moveLeft());

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

        Set<Point> oldTileLocations = t.getTileLocations();
        assertFalse(t.moveLeft());

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveLeftSpaceOccupiedByTile() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            t.moveDown();
        }
        t.moveLeft();
        t.moveLeft();
        t.moveLeft();

        Set<Point> tileLocations = t.getTileLocations();
        assertFalse(t.moveLeft());
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    public void testMoveRightNotBoundary() {
        Set<Point> oldTileLocations = t.getTileLocations();
        assertTrue(t.moveRight());

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

        Set<Point> oldTileLocations = t.getTileLocations();
        assertFalse(t.moveRight());

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveRightSpaceOccupiedByTile() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            t.moveDown();
        }
        t.moveRight();
        t.moveRight();

        Set<Point> tileLocations = t.getTileLocations();
        assertFalse(t.moveRight());
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    public void testMoveDownNotBoundary() {
        Set<Point> oldTileLocations = t.getTileLocations();
        assertTrue(t.moveDown());

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

        Set<Point> oldTileLocations = t.getTileLocations();
        assertFalse(t.moveDown());

        Set<Point> newTileLocations = t.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveDownSpaceOccupiedByTile() {
        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveLeft();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            t.moveDown();
        }

        // t is guaranteed to be directly above a tile now.

        Set<Point> tileLocations = t.getTileLocations();
        assertFalse(t.moveDown());
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    public void testRotateAtCeiling() {
        Set<Point> tileLocations = t.getTileLocations();
        if (t instanceof OPiece) {
            assertTrue(t.rotate());
        } else {
            assertFalse(t.rotate());
        }
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    public void testGetTileLocationsChangingReturnedSet() {
        Set<Point> tileLocations = t.getTileLocations();
        tileLocations.add(new Point(2 * Game.WIDTH, 2 * Game.HEIGHT));

        // Changing tileLocations should not change the actual locations
        // of the tiles of t.
        assertNotEquals(tileLocations, t.getTileLocations());
    }

    @Test
    public abstract void testRotateInFreeSpace();

    @Test
    public abstract void testRotateAtWall();

    @Test
    public abstract void testRotateWithObstructingTiles();
}
