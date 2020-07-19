package model;

import model.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game g;
    private static final int GAME_SEED = 23352;

    @BeforeEach
    public void setUp() {
        g = new Game();
        g.startNewGame(GAME_SEED);

        // First ten numbers that are produced by the random number generator with seed 23352:
        // 4, 2, 0, 1, 6, 3, 5, 0, 5, 4
    }

    @Test
    public void testStartNewGame() {
        assertEquals(0, g.getScore());
        assertEquals(0, g.getLinesCleared());
        assertFalse(g.isGameOver());
        assertNotNull(g.getActiveTetromino());
        assertNotNull(g.getNextTetromino());

        // The first piece is an S piece

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, 1);
        Point point2 = new Point(point1XPos + 1, 1);
        Point point3 = new Point(point1XPos + 1, 0);
        Point point4 = new Point(point1XPos + 2, 0);

        checkTetrominoHasTileLocations(g.getActiveTetromino(), point1, point2, point3, point4);

        // Check that the S piece shows up on the board
        checkBoardContainsTetromino(g.getBoard(), g.getActiveTetromino());

        assertTrue(g.getNextTetromino() instanceof LPiece);
    }

    @Test
    public void testUpdateMoveDownOnce() {
        // We first make a copy of the current set of tile locations for the active tetromino.
        // We must make this copy because we do not want oldTileLocations to change
        // when we call g.update().
        Set<Point> oldTileLocations = new HashSet<Point>();
        for (Point location : g.getActiveTetromino().getTileLocations()) {
            oldTileLocations.add(new Point(location));
        }

        g.update();

        Set<Point> newTileLocations = g.getActiveTetromino().getTileLocations();

        // Make sure that each point in newTileLocations corresponds to a point
        // in oldTileLocations moved down by 1 unit. This ensures the correctness
        // of newTileLocations.
        for (Point location : newTileLocations) {
            Point oldPoint = new Point(location.x, location.y - 1);

            // This call works because the Point class overrides equals.
            assertTrue(oldTileLocations.contains(oldPoint));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());

        // Check that the tetromino does not somehow disappear from the board:
        List<ArrayList<Boolean>> board = g.getBoard();
        checkBoardContainsTetromino(board, g.getActiveTetromino());

        // Check that there are only four tiles on the board:
        assertEquals(4, getNumTilesOnBoard());
    }

    @Test
    public void testUpdateTetrominoLandsAtBottom() {
        // The first piece is an S piece.
        for (int i = 1; i <= Game.HEIGHT - 2; i++) {
            g.update();
        }
        // The S piece should now be just about to land at the bottom
        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, Game.HEIGHT - 1);
        Point point2 = new Point(point1XPos + 1, Game.HEIGHT - 1);
        Point point3 = new Point(point1XPos + 1, Game.HEIGHT - 2);
        Point point4 = new Point(point1XPos + 2, Game.HEIGHT - 2);

        checkTetrominoHasTileLocations(g.getActiveTetromino(), point1, point2, point3, point4);
        checkBoardContainsTetromino(g.getBoard(), g.getActiveTetromino());

        // Update the game once more:

        g.update();

        // At this point, the S piece should have landed at the bottom, and
        // the next piece (an L piece) will have just spawned in.

        point2.x = point1XPos;
        point1.y = 1;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkTetrominoHasTileLocations(g.getActiveTetromino(), point1, point2, point3, point4);
        checkBoardContainsTetromino(g.getBoard(), g.getActiveTetromino());

        assertTrue(g.getNextTetromino() instanceof IPiece);
    }

    @Test
    public void testUpdateTetrominoLandsOnTetromino() {
        // Get the S piece to land at the bottom, then get the L piece to
        // be just about to land on the S piece.
        for (int i = 1; i <= 2 * Game.HEIGHT - 4; i++) {
            g.update();
        }

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, Game.HEIGHT - 2);
        Point point2 = new Point(point1XPos, Game.HEIGHT - 3);
        Point point3 = new Point(point1XPos + 1, Game.HEIGHT - 3);
        Point point4 = new Point(point1XPos + 2, Game.HEIGHT - 3);

        checkTetrominoHasTileLocations(g.getActiveTetromino(), point1, point2, point3, point4);
        checkBoardContainsTetromino(g.getBoard(), g.getActiveTetromino());

        // Update the game once more, causing the L piece to land on the S piece:

        g.update();

        // The I piece should have spawned in now.

        point1.x = point1XPos - 1;
        point1.y = 0;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkTetrominoHasTileLocations(g.getActiveTetromino(), point1, point2, point3, point4);
        checkBoardContainsTetromino(g.getBoard(), g.getActiveTetromino());
    }

    @Test
    public void testUpdateSingleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH; i++) {
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        g.setBoard(riggedBoard);
        g.update();

        assertEquals(Game.SINGLE_POINTS, g.getScore());
        assertEquals(1, g.getLinesCleared());
        for (int i = 0; i < Game.WIDTH; i++) {
            assertFalse(riggedBoard.get(Game.HEIGHT - 1).get(i));
        }
    }

    @Test
    public void testUpdateDoubleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH; i++) {
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        g.setBoard(riggedBoard);
        g.update();

        assertEquals(Game.DOUBLE_POINTS, g.getScore());
        assertEquals(2, g.getLinesCleared());
        for (int i = 0; i < Game.WIDTH; i++) {
            assertFalse(riggedBoard.get(Game.HEIGHT - 2).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 1).get(i));
        }
    }

    @Test
    public void testUpdateTripleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH; i++) {
            riggedBoard.get(Game.HEIGHT - 3).set(i, true);
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        g.setBoard(riggedBoard);
        g.update();

        assertEquals(Game.TRIPLE_POINTS, g.getScore());
        assertEquals(3, g.getLinesCleared());
        for (int i = 0; i < Game.WIDTH; i++) {
            assertFalse(riggedBoard.get(Game.HEIGHT - 3).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 2).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 1).get(i));
        }
    }

    @Test
    public void testUpdateTetrisLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH; i++) {
            riggedBoard.get(Game.HEIGHT - 4).set(i, true);
            riggedBoard.get(Game.HEIGHT - 3).set(i, true);
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        g.setBoard(riggedBoard);
        g.update();

        assertEquals(Game.TETRIS_POINTS, g.getScore());
        assertEquals(4, g.getLinesCleared());
        for (int i = 0; i < Game.WIDTH; i++) {
            assertFalse(riggedBoard.get(Game.HEIGHT - 4).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 3).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 2).get(i));
            assertFalse(riggedBoard.get(Game.HEIGHT - 1).get(i));
        }
    }

    @Test
    public void testUpdateLineClearsWithLinesAbove() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (j >= Game.HEIGHT - 5) {
                    riggedBoard.get(j).set(i, true);
                }
            }
        }
        for (int i = 3; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (j == Game.HEIGHT - 2 || j == Game.HEIGHT - 4) {
                    riggedBoard.get(j).set(i, true);
                }
            }
        }
        riggedBoard.get(Game.HEIGHT - 6).set(5, true);

        g.setBoard(riggedBoard);
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.DOUBLE_POINTS, g.getScore());
        for (int i = 0; i < 3; i++) {
            for (int j = Game.HEIGHT - 3; j < Game.HEIGHT; j++) {
                assertTrue(updatedBoard.get(j).get(i));
            }
        }
        assertTrue(updatedBoard.get(Game.HEIGHT - 4).get(5));
        assertFalse(updatedBoard.get(Game.HEIGHT - 6).get(5));
    }

    @Test
    public void testUpdateGameOver() {
        List<ArrayList<Boolean>> gameOverBoard = new ArrayList<ArrayList<Boolean>>();
        for (int r = 0; r < Game.HEIGHT; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH; c++) {
                if (r == 0 || r == 1 || c == 0) {
                    row.add(false);
                } else {
                    row.add(true);
                }
            }
            gameOverBoard.add(row);
        }

        g.setBoard(gameOverBoard);
        g.update(); // S piece lands at the top
        assertFalse(g.isGameOver());

        g.update(); // L piece has nowhere to go, so player tops out
        assertTrue(g.isGameOver());

        List<ArrayList<Boolean>> board = g.getBoard();
        List<ArrayList<Boolean>> boardCopy = new ArrayList<ArrayList<Boolean>>();

        for (int i = 0; i < Game.HEIGHT; i++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int j = 0; j < Game.WIDTH; j++) {
                row.add(board.get(i).get(j));
            }
            boardCopy.add(row);
        }

        // Should do nothing
        g.update();

        board = g.getBoard();

        // Check that the board did not change
        for (int i = 0; i < Game.HEIGHT; i++) {
            for (int j = 0; j < Game.WIDTH; j++) {
                assertEquals(board.get(i).get(j), boardCopy.get(i).get(j));
            }
        }
    }

    @Test
    public void testUpdateTetrominoGeneration() {
        // The purpose of this test is to make sure that the random tetromino generator
        // behaves properly.

        boolean seenIPiece = false;
        boolean seenJPiece = false;
        boolean seenLPiece = false;
        boolean seenOPiece = false;
        boolean seenSPiece = false;
        boolean seenTPiece = false;
        boolean seenZPiece = false;

        // With the specific rng seed 23352, we will have seen all the different
        // piece types after 7 * Game.HEIGHT - 39 game updates.
        for (int i = 0; i < 7 * Game.HEIGHT - 39; i++) {
            Tetromino activeTetromino = g.getActiveTetromino();
            if (!seenIPiece && activeTetromino instanceof IPiece) {
                seenIPiece = true;
            } else if (!seenJPiece && activeTetromino instanceof JPiece) {
                seenJPiece = true;
            } else if (!seenLPiece && activeTetromino instanceof LPiece) {
                seenLPiece = true;
            } else if (!seenOPiece && activeTetromino instanceof OPiece) {
                seenOPiece = true;
            } else if (!seenSPiece && activeTetromino instanceof SPiece) {
                seenSPiece = true;
            } else if (!seenTPiece && activeTetromino instanceof TPiece) {
                seenTPiece = true;
            } else if (!seenZPiece && activeTetromino instanceof ZPiece) {
                seenZPiece = true;
            }
        }

        assertTrue(seenIPiece);
        assertTrue(seenJPiece);
        assertTrue(seenLPiece);
        assertTrue(seenOPiece);
        assertTrue(seenSPiece);
        assertTrue(seenTPiece);
        assertTrue(seenZPiece);
    }

    @Test
    public void testGetBlankBoard() {
        List<ArrayList<Boolean>> blankBoard = Game.getBlankBoard();
        assertEquals(Game.HEIGHT, blankBoard.size());
        for (int i = 0; i < blankBoard.size(); i++) {
            List<Boolean> row = blankBoard.get(i);
            assertEquals(Game.WIDTH, row.size());
            for (int j = 0; j < row.size(); j++) {
                assertFalse(row.get(j));
            }
        }
    }

    // EFFECTS: Ensures that the given board contains the given tetromino
    public static void checkBoardContainsTetromino(List<ArrayList<Boolean>> boardCells, Tetromino tetromino) {
        for (Point p : tetromino.getTileLocations()) {
            assertTrue(boardCells.get(p.y).get(p.x));
        }
    }

    // EFFECTS: Ensures that the tetromino's tiles are located at the four given points
    public static void checkTetrominoHasTileLocations(Tetromino tetromino,
                                                       Point point1,
                                                       Point point2,
                                                       Point point3,
                                                       Point point4) {
        Set<Point> tetrominoTileLocations = tetromino.getTileLocations();
        assertTrue(tetrominoTileLocations.contains(point1));
        assertTrue(tetrominoTileLocations.contains(point2));
        assertTrue(tetrominoTileLocations.contains(point3));
        assertTrue(tetrominoTileLocations.contains(point4));
        assertEquals(4, tetrominoTileLocations.size());
    }

    // EFFECTS: returns the number of tiles on the game board
    private int getNumTilesOnBoard() {
        int numTiles = 0;
        for (List<Boolean> column : g.getBoard()) {
            for (boolean cellOccupied : column) {
                if (cellOccupied) {
                    numTiles++;
                }
            }
        }
        return numTiles;
    }
}