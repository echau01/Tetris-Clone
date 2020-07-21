package test.model.pieces;

import model.Game;
import model.pieces.OPiece;
import org.junit.jupiter.api.Assertions;
import test.model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

// Unit tests for the OPiece class
public class OPieceTest extends PieceTest {

    @BeforeEach
    public void setUpPiece() {
        piece = new OPiece(super.testGame);
    }

    @Test
    @Override
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 0);
        Point point2 = new Point(approximateCenter, 1);
        Point point3 = new Point(approximateCenter + 1, 0);
        Point point4 = new Point(approximateCenter + 1, 1);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
    }

    /* The O piece is invariant under rotations */

    @Test
    @Override
    public void testRotateInFreeSpace() {
        for (int i = 0; i < 4; i++) {
            Set<Point> tileLocations = piece.getTileLocations();
            Assertions.assertTrue(piece.rotate());
            Assertions.assertEquals(tileLocations, piece.getTileLocations());
        }
    }

    @Test
    @Override
    public void testRotateAtWall() {
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }
        Set<Point> tileLocations = piece.getTileLocations();
        Assertions.assertTrue(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());
    }

    @Test
    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        piece.moveRight();
        piece.moveRight();
        piece.moveRight();

        Set<Point> tileLocations = piece.getTileLocations();
        Assertions.assertTrue(piece.rotate());
        Assertions.assertEquals(tileLocations, piece.getTileLocations());
    }
}
