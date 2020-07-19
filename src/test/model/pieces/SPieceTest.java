package model.pieces;

import model.Game;
import model.GameTest;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SPieceTest extends TetrominoTest {
    private SPiece piece;

    @BeforeEach
    public void setUpPiece() {
        piece = new SPiece(super.testGame);
    }

    @Override
    public void testRotateInFreeSpace() {
        piece.moveDown();

        assertTrue(piece.rotate());
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 0);
        Point point2 = new Point(approximateCenter, 1);
        Point point3 = new Point(approximateCenter + 1, 1);
        Point point4 = new Point(approximateCenter + 1, 2);

        GameTest.checkTetrominoHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter;
        point1.y = 2;
        point2.x = approximateCenter + 1;
        point2.y = 2;
        point3.x = approximateCenter + 1;
        point3.y = 1;
        point4.x = approximateCenter + 2;
        point4.y = 1;

        GameTest.checkTetrominoHasTileLocations(piece, point1, point2, point3, point4);
    }

    @Override
    public void testRotateAtWall() {
        piece.moveDown();
        assertTrue(piece.rotate());
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveRight();
        }
        assertFalse(piece.rotate());
    }

    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        assertTrue(piece.rotate());
        piece.moveRight();
        piece.moveRight();
        piece.moveRight();
        assertFalse(piece.rotate());
    }
}
