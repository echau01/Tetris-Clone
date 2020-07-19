package model.pieces;

import model.Game;
import model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OPieceTest extends TetrominoTest {

    @BeforeEach
    public void setUpPiece() {
        t = new OPiece(super.testGame);
    }

    @Test
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 0);
        Point point2 = new Point(approximateCenter, 1);
        Point point3 = new Point(approximateCenter + 1, 0);
        Point point4 = new Point(approximateCenter + 1, 1);

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);
    }

    /* The O piece is invariant under rotations */

    @Test
    @Override
    public void testRotateInFreeSpace() {
        Set<Point> tileLocations = t.getTileLocations();
        assertTrue(t.rotate());
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    @Override
    public void testRotateAtWall() {
        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveLeft();
        }
        Set<Point> tileLocations = t.getTileLocations();
        assertTrue(t.rotate());
        assertEquals(tileLocations, t.getTileLocations());
    }

    @Test
    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            t.moveDown();
        }
        t.moveRight();
        t.moveRight();
        t.moveRight();

        Set<Point> tileLocations = t.getTileLocations();
        assertTrue(t.rotate());
        assertEquals(tileLocations, t.getTileLocations());
    }
}
