package model.pieces;

import model.Game;
import model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TPieceTest extends PieceTest {

    @BeforeEach
    public void setUpPiece() {
        piece = new TPiece(super.testGame);
    }

    @Test
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 0);
        Point point2 = new Point(approximateCenter + 1, 0);
        Point point3 = new Point(approximateCenter + 2, 0);
        Point point4 = new Point(approximateCenter + 1, 1);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateInFreeSpace() {
        piece.moveDown();

        assertTrue(piece.rotate());
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter + 1, 0);
        Point point2 = new Point(approximateCenter + 1, 1);
        Point point3 = new Point(approximateCenter + 1, 2);
        Point point4 = new Point(approximateCenter, 1);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter + 2;
        point1.y = 2;
        point2.x = approximateCenter + 1;
        point2.y = 2;
        point3.x = approximateCenter;
        point3.y = 2;
        point4.x = approximateCenter + 1;
        point4.y = 1;

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter + 1;
        point1.y = 2;
        point2.x = approximateCenter + 1;
        point2.y = 1;
        point3.x = approximateCenter + 1;
        point3.y = 0;
        point4.x = approximateCenter + 2;
        point4.y = 1;

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter;
        point1.y = 1;
        point2.x = approximateCenter + 1;
        point2.y = 1;
        point3.x = approximateCenter + 2;
        point3.y = 1;
        point4.x = approximateCenter + 1;
        point4.y = 2;

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateAtWall() {
        piece.moveDown();
        assertTrue(piece.rotate());
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveRight();
        }
        assertFalse(piece.rotate());

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }

        assertTrue(piece.rotate());
        assertTrue(piece.rotate());
        piece.moveLeft();
        assertFalse(piece.rotate());
    }

    @Test
    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        piece.moveRight();
        piece.moveRight();
        assertTrue(piece.rotate());
        piece.moveRight();
        assertFalse(piece.rotate());
    }
}
