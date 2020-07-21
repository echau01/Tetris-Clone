package test.model.pieces;

import model.Game;
import model.pieces.IPiece;
import org.junit.jupiter.api.Assertions;
import test.model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

// Unit tests for the IPiece class
public class IPieceTest extends PieceTest {

    @BeforeEach
    public void setUpPiece() {
        piece = new IPiece(super.testGame);
    }

    @Test
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter - 1, 0);
        Point point2 = new Point(approximateCenter, 0);
        Point point3 = new Point(approximateCenter + 1, 0);
        Point point4 = new Point(approximateCenter + 2, 0);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateInFreeSpace() {
        piece.moveDown();
        for (int i = 0; i < 2; i++) {
            Assertions.assertTrue(piece.rotate());

            int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

            Point point1 = new Point(approximateCenter + 1, 0);
            Point point2 = new Point(approximateCenter + 1, 1);
            Point point3 = new Point(approximateCenter + 1, 2);
            Point point4 = new Point(approximateCenter + 1, 3);

            GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

            Assertions.assertTrue(piece.rotate());

            point1.x = approximateCenter - 1;
            point1.y = 1;
            point2.x = approximateCenter;
            point2.y = 1;
            point3.x = approximateCenter + 1;
            point3.y = 1;
            point4.x = approximateCenter + 2;
            point4.y = 1;

            GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
        }
    }

    @Test
    public void testRotateAtFloor() {
        for (int i = 1; i <= Game.HEIGHT - 2; i++) {
            piece.moveDown();
        }
        Set<Point> tileLocations = piece.getTileLocations();
        Assertions.assertFalse(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    @Override
    public void testRotateAtWall() {
        piece.moveDown();
        Assertions.assertTrue(piece.rotate()); // make the piece upright

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }

        Set<Point> tileLocations = piece.getTileLocations();
        Assertions.assertFalse(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveRight();
        }

        tileLocations = piece.getTileLocations();
        Assertions.assertFalse(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    @Override
    public void testRotateWithObstructingTiles() {
        piece.moveDown();
        Assertions.assertTrue(piece.rotate());
        piece.moveRight();

        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }

        Set<Point> tileLocations = piece.getTileLocations();
        Assertions.assertFalse(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());
    }
}
