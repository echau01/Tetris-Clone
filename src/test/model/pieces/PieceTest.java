package model.pieces;

import exceptions.IncorrectBoardSizeException;
import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the Piece class
public abstract class PieceTest {
    protected static int TEST_GAME_WALL_HEIGHT = Game.HEIGHT / 2;

    protected Piece piece;

    // Do not call update on this game!
    protected Game testGame;

    // We want this @BeforeEach method to run before the @BeforeEach methods in the subclasses of
    // this class. Fortunately, https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html
    // tells us that this behaviour will happen.
    @BeforeEach
    public void setUpTestGame() {
        testGame = new Game(0);

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

        try {
            testGame.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }
    }

    @Test
    public abstract void testConstructor();

    @Test
    public void testMoveLeftNotBoundary() {
        Set<Point> oldTileLocations = piece.getTileLocations();
        assertTrue(piece.moveLeft());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x + 1, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveLeftWallBoundary() {
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }

        // By now, t is guaranteed to be at the left wall

        Set<Point> oldTileLocations = piece.getTileLocations();
        assertFalse(piece.moveLeft());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveLeftSpaceOccupiedByTile() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        piece.moveLeft();
        piece.moveLeft();
        piece.moveLeft();

        Set<Point> tileLocations = piece.getTileLocations();
        assertFalse(piece.moveLeft());
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    public void testMoveRightNotBoundary() {
        Set<Point> oldTileLocations = piece.getTileLocations();
        assertTrue(piece.moveRight());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x - 1, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveRightWallBoundary() {
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveRight();
        }

        // By now, t is guaranteed to be at the right wall

        Set<Point> oldTileLocations = piece.getTileLocations();
        assertFalse(piece.moveRight());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveRightSpaceOccupiedByTile() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        piece.moveRight();
        piece.moveRight();

        Set<Point> tileLocations = piece.getTileLocations();
        assertFalse(piece.moveRight());
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    public void testMoveDownNotBoundary() {
        Set<Point> oldTileLocations = piece.getTileLocations();
        assertTrue(piece.moveDown());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y - 1)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveDownFloorBoundary() {
        for (int i = 0; i < Game.HEIGHT; i++) {
            piece.moveDown();
        }

        // By now, t is guaranteed to be at the floor

        Set<Point> oldTileLocations = piece.getTileLocations();
        assertFalse(piece.moveDown());

        Set<Point> newTileLocations = piece.getTileLocations();
        for (Point location : newTileLocations) {
            assertTrue(oldTileLocations.contains(new Point(location.x, location.y)));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());
    }

    @Test
    public void testMoveDownSpaceOccupiedByTile() {
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            piece.moveDown();
        }

        // t is guaranteed to be directly above a tile now.

        Set<Point> tileLocations = piece.getTileLocations();
        assertFalse(piece.moveDown());
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    public void testRotateAtCeiling() {
        Set<Point> tileLocations = piece.getTileLocations();
        if (piece instanceof OPiece) {
            assertTrue(piece.rotate());
        } else {
            assertFalse(piece.rotate());
        }
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    public void testGetTileLocationsChangingReturnedSet() {
        Set<Point> tileLocations = piece.getTileLocations();
        tileLocations.add(new Point(2 * Game.WIDTH, 2 * Game.HEIGHT));

        // Changing tileLocations should not change the actual locations
        // of the tiles of t.
        assertNotEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    public abstract void testRotateInFreeSpace();

    @Test
    public abstract void testRotateAtWall();

    @Test
    public abstract void testRotateWithObstructingTiles();
}
