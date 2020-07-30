package model.pieces;

import model.Game;
import org.junit.jupiter.api.Assertions;
import model.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Unit tests for the JPiece class
public class JPieceTest extends PieceTest {

    @BeforeEach
    public void setUpPiece() {
        piece = new JPiece(super.testGame);
    }

    @Test
    @Override
    public void testConstructor() {
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter + 2, 1);
        Point point2 = new Point(approximateCenter + 2, 0);
        Point point3 = new Point(approximateCenter + 1, 0);
        Point point4 = new Point(approximateCenter, 0);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);
    }

    @Test
    @Override
    public void testRotateInFreeSpace() {
        piece.moveDown();

        assertTrue(piece.rotate());
        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter, 2);
        Point point2 = new Point(approximateCenter + 1, 2);
        Point point3 = new Point(approximateCenter + 1, 1);
        Point point4 = new Point(approximateCenter + 1, 0);

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter;
        point1.y = 1;
        point2.x = approximateCenter;
        point2.y = 2;
        point3.x = approximateCenter + 1;
        point3.y = 2;
        point4.x = approximateCenter + 2;
        point4.y = 2;

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter + 2;
        point1.y = 0;
        point2.x = approximateCenter + 1;
        point2.y = 0;
        point3.x = approximateCenter + 1;
        point3.y = 1;
        point4.x = approximateCenter + 1;
        point4.y = 2;

        GameTest.checkPieceHasTileLocations(piece, point1, point2, point3, point4);

        assertTrue(piece.rotate());

        point1.x = approximateCenter + 2;
        point1.y = 2;
        point2.x = approximateCenter + 2;
        point2.y = 1;
        point3.x = approximateCenter + 1;
        point3.y = 1;
        point4.x = approximateCenter;
        point4.y = 1;

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
        Assertions.assertFalse(piece.rotate());

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }

        assertTrue(piece.rotate());
        assertTrue(piece.rotate());
        piece.moveLeft();
        Assertions.assertFalse(piece.rotate());
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
        Assertions.assertFalse(piece.rotate());
    }

    /* The following two tests are for testing Piece's getHardDropTileLocations method. */

    @Test
    public void testGetHardDropTileLocationsPieceLandsOnFloor() {
        List<ArrayList<Boolean>> board = testGame.getBoard();
        Set<Point> preTileLocations = piece.getTileLocations();
        Set<Point> hardDropTileLocations = piece.getHardDropTileLocations();

        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter + 2, Game.HEIGHT - 1);
        Point point2 = new Point(approximateCenter + 2, Game.HEIGHT - 2);
        Point point3 = new Point(approximateCenter + 1, Game.HEIGHT - 2);
        Point point4 = new Point(approximateCenter, Game.HEIGHT - 2);

        Set<Point> points = new HashSet<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);

        assertEquals(points, hardDropTileLocations);

        // Check that the game board and piece tile locations did not change
        assertEquals(preTileLocations, piece.getTileLocations());
        assertTrue(GameTest.listsOfArrayListsEqual(board, testGame.getBoard()));
    }

    @Test
    public void testGetHardDropTileLocationsPieceLandsOnTile() {
        piece.moveDown();
        piece.rotate();

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }

        // The "J" piece is now oriented as an upright "J" and is at the left edge of the board.
        List<ArrayList<Boolean>> board = testGame.getBoard();
        Set<Point> preTileLocations = piece.getTileLocations();

        Set<Point> hardDropTileLocations = piece.getHardDropTileLocations();

        Point point1 = new Point(0, Game.HEIGHT / 2 - 1);
        Point point2 = new Point(1, Game.HEIGHT / 2 - 1);
        Point point3 = new Point(1, Game.HEIGHT / 2 - 2);
        Point point4 = new Point(1, Game.HEIGHT / 2 - 3);

        Set<Point> points = new HashSet<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);

        assertEquals(points, hardDropTileLocations);

        // Check that the game board and piece tile locations did not change
        assertEquals(preTileLocations, piece.getTileLocations());
        assertTrue(GameTest.listsOfArrayListsEqual(board, testGame.getBoard()));
    }

    @Test
    public void testGetHardDropTileLocationsPieceAboutToLandOnFloor() {
        for (int i = 0; i < Game.HEIGHT; i++) {
            piece.moveDown();
        }
        List<ArrayList<Boolean>> board = testGame.getBoard();
        Set<Point> preTileLocations = piece.getTileLocations();
        Set<Point> hardDropTileLocations = piece.getHardDropTileLocations();

        int approximateCenter = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(approximateCenter + 2, Game.HEIGHT - 1);
        Point point2 = new Point(approximateCenter + 2, Game.HEIGHT - 2);
        Point point3 = new Point(approximateCenter + 1, Game.HEIGHT - 2);
        Point point4 = new Point(approximateCenter, Game.HEIGHT - 2);

        Set<Point> points = new HashSet<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);

        assertEquals(points, hardDropTileLocations);

        // Check that the game board and piece tile locations did not change
        assertEquals(preTileLocations, piece.getTileLocations());
        assertTrue(GameTest.listsOfArrayListsEqual(board, testGame.getBoard()));
    }

    @Test
    public void testGetHardDropTileLocationsPieceAboutToLandOnTile() {
        piece.moveDown();
        piece.rotate();

        for (int i = 0; i < Game.WIDTH; i++) {
            piece.moveLeft();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            piece.moveDown();
        }

        // The "J" piece is now oriented as an upright "J" and is at the left edge of the board,
        // sitting just above a tile.
        List<ArrayList<Boolean>> board = testGame.getBoard();
        Set<Point> preTileLocations = piece.getTileLocations();

        Set<Point> hardDropTileLocations = piece.getHardDropTileLocations();

        Point point1 = new Point(0, Game.HEIGHT / 2 - 1);
        Point point2 = new Point(1, Game.HEIGHT / 2 - 1);
        Point point3 = new Point(1, Game.HEIGHT / 2 - 2);
        Point point4 = new Point(1, Game.HEIGHT / 2 - 3);

        Set<Point> points = new HashSet<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);

        assertEquals(points, hardDropTileLocations);

        // Check that the game board and piece tile locations did not change
        assertEquals(preTileLocations, piece.getTileLocations());
        assertTrue(GameTest.listsOfArrayListsEqual(board, testGame.getBoard()));
    }
}
