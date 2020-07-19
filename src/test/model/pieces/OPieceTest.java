package model.pieces;

import model.Game;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OPieceTest extends TetrominoTest {
    private OPiece piece;

    @BeforeEach
    public void setUpPiece() {
        piece = new OPiece(super.testGame);
    }

    /* The O piece is invariant under rotations */

    @Override
    public void testRotateInFreeSpace() {
        Set<Point> tileLocations = super.getTileLocationsCopy(piece);
        assertTrue(piece.rotate());
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Override
    public void testRotateAtWall() {
        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }
        Set<Point> tileLocations = super.getTileLocationsCopy(piece);
        assertTrue(piece.rotate());
        assertEquals(tileLocations, piece.getTileLocations());
    }

    @Override
    public void testRotateWithObstructingTiles() {
        for (int i = 0; i < TEST_GAME_WALL_HEIGHT; i++) {
            piece.moveDown();
        }
        piece.moveRight();
        piece.moveRight();
        piece.moveRight();

        Set<Point> tileLocations = super.getTileLocationsCopy(piece);
        assertTrue(piece.rotate());
        assertEquals(tileLocations, piece.getTileLocations());
    }
}
