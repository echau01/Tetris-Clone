package model.pieces;

import model.Game;
import model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TPieceTest extends TetrominoTest {

    @BeforeEach
    public void setUpPiece() {
        t = new TPiece(super.testGame);
    }

    @Test
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 0);
        Point point2 = new Point(approximateCenter + 1, 0);
        Point point3 = new Point(approximateCenter + 2, 0);
        Point point4 = new Point(approximateCenter + 1, 1);

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateInFreeSpace() {
        t.moveDown();

        assertTrue(t.rotate());
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter + 1, 0);
        Point point2 = new Point(approximateCenter + 1, 1);
        Point point3 = new Point(approximateCenter + 1, 2);
        Point point4 = new Point(approximateCenter, 1);

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);

        assertTrue(t.rotate());

        point1.x = approximateCenter + 2;
        point1.y = 2;
        point2.x = approximateCenter + 1;
        point2.y = 2;
        point3.x = approximateCenter;
        point3.y = 2;
        point4.x = approximateCenter + 1;
        point4.y = 1;

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);

        point1.x = approximateCenter + 1;
        point1.y = 2;
        point2.x = approximateCenter + 1;
        point2.y = 1;
        point3.x = approximateCenter + 1;
        point3.y = 0;
        point4.x = approximateCenter + 2;
        point4.y = 1;

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);

        point1.x = approximateCenter;
        point1.y = 1;
        point2.x = approximateCenter + 1;
        point2.y = 1;
        point3.x = approximateCenter + 2;
        point3.y = 1;
        point4.x = approximateCenter + 1;
        point4.y = 2;

        GameTest.checkTetrominoHasTileLocations(t, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateAtWall() {
        t.moveDown();
        assertTrue(t.rotate());
        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveRight();
        }
        assertFalse(t.rotate());

        for (int i = 0; i < Game.WIDTH; i++) {
            t.moveLeft();
        }

        assertTrue(t.rotate());
        assertTrue(t.rotate());
        t.moveLeft();
        assertFalse(t.rotate());
    }

    @Test
    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            t.moveDown();
        }
        t.moveRight();
        t.moveRight();
        assertTrue(t.rotate());
        t.moveRight();
        assertFalse(t.rotate());
    }
}
