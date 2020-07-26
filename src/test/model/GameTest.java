package model;

import exceptions.IncorrectBoardSizeException;
import model.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the Game class
public class GameTest {
    private Game testGame1;
    private Game testGame2;
    private static final int GAME_SEED_ONE = 23352;
    private static final int GAME_SEED_TWO = 9;

    @BeforeEach
    public void setUp() {
        // First ten numbers that are produced by the random number generator with seed 23352:
        // 4, 2, 0, 1, 6, 3, 5, 0, 5, 4
        testGame1 = new Game(GAME_SEED_ONE);

        // The first number randomly generated with seed 9 is 0.
        testGame2 = new Game(GAME_SEED_TWO);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testGame1.getScore());
        assertEquals(0, testGame1.getLinesCleared());
        assertFalse(testGame1.isGameOver());
        assertNotNull(testGame1.getActivePiece());
        assertNotNull(testGame1.getNextPiece());

        // The first piece is an S piece

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, 1);
        Point point2 = new Point(point1XPos + 1, 1);
        Point point3 = new Point(point1XPos + 1, 0);
        Point point4 = new Point(point1XPos + 2, 0);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);

        // Check that the S piece shows up on the board
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        assertTrue(testGame1.getNextPiece() instanceof LPiece);
    }

    @Test
    public void testUpdateMoveDownOnce() {
        // We first make a copy of the current set of tile locations for the active piece.
        // We must make this copy because we do not want oldTileLocations to change
        // when we call testGame.update().
        Set<Point> oldTileLocations = new HashSet<Point>();
        for (Point location : testGame1.getActivePiece().getTileLocations()) {
            oldTileLocations.add(new Point(location));
        }

        testGame1.update();

        Set<Point> newTileLocations = testGame1.getActivePiece().getTileLocations();

        // Make sure that each point in newTileLocations corresponds to a point
        // in oldTileLocations moved down by 1 unit. This ensures the correctness
        // of newTileLocations.
        for (Point location : newTileLocations) {
            Point oldPoint = new Point(location.x, location.y - 1);

            // This call works because the Point class overrides equals.
            assertTrue(oldTileLocations.contains(oldPoint));
        }

        assertEquals(oldTileLocations.size(), newTileLocations.size());

        // Check that the piece does not somehow disappear from the board:
        List<ArrayList<Boolean>> board = testGame1.getBoard();
        checkBoardContainsPiece(board, testGame1.getActivePiece());

        // Check that there are only four tiles on the board:
        assertEquals(4, getNumTilesOnBoard());
    }

    @Test
    public void testUpdatePieceLandsAtBottom() {
        // The first piece is an S piece.
        for (int i = 1; i <= Game.HEIGHT - 2; i++) {
            testGame1.update();
        }
        // The S piece should now be just about to land at the bottom
        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, Game.HEIGHT - 1);
        Point point2 = new Point(point1XPos + 1, Game.HEIGHT - 1);
        Point point3 = new Point(point1XPos + 1, Game.HEIGHT - 2);
        Point point4 = new Point(point1XPos + 2, Game.HEIGHT - 2);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        // Update the game once more:

        testGame1.update();

        // At this point, the S piece should have landed at the bottom, and
        // the next piece (an L piece) will have just spawned in.

        point2.x = point1XPos;
        point1.y = 1;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        assertTrue(testGame1.getNextPiece() instanceof IPiece);
    }

    @Test
    public void testUpdatePieceLandsOnPiece() {
        // Get the S piece to land at the bottom, then get the L piece to
        // be just about to land on the S piece.
        for (int i = 1; i <= 2 * Game.HEIGHT - 4; i++) {
            testGame1.update();
        }

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, Game.HEIGHT - 2);
        Point point2 = new Point(point1XPos, Game.HEIGHT - 3);
        Point point3 = new Point(point1XPos + 1, Game.HEIGHT - 3);
        Point point4 = new Point(point1XPos + 2, Game.HEIGHT - 3);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        // Update the game once more, causing the L piece to land on the S piece:

        testGame1.update();

        // The "I" piece should have spawned in now.

        point1.x = point1XPos - 1;
        point1.y = 0;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());
    }

    @Test
    public void testUpdateSingleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH - 1; i++) {
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now put this "I" piece in the bottom right corner, standing upright.
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
        testGame2.update();

        List<ArrayList<Boolean>> updatedBoard = testGame2.getBoard();
        assertEquals(Game.SINGLE_POINTS, testGame2.getScore());
        assertEquals(1, testGame2.getLinesCleared());
        for (int c = 0; c <= Game.WIDTH - 2; c++) {
            assertFalse(updatedBoard.get(Game.HEIGHT - 1).get(c));
        }

        // Check that the "I" piece moved down 1 row
        assertFalse(updatedBoard.get(Game.HEIGHT - 4).get(Game.WIDTH - 1));
        for (int r = Game.HEIGHT - 3; r <= Game.HEIGHT - 1; r++) {
            assertTrue(updatedBoard.get(r).get(Game.WIDTH - 1));
        }
    }

    @Test
    public void testUpdateDoubleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH - 1; i++) {
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now put this "I" piece in the bottom right corner, standing upright.
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
        testGame2.update();

        List<ArrayList<Boolean>> updatedBoard = testGame2.getBoard();
        assertEquals(Game.DOUBLE_POINTS, testGame2.getScore());
        assertEquals(2, testGame2.getLinesCleared());
        for (int c = 0; c <= Game.WIDTH - 2; c++) {
            assertFalse(updatedBoard.get(Game.HEIGHT - 2).get(c));
            assertFalse(updatedBoard.get(Game.HEIGHT - 1).get(c));
        }

        // Check that the "I" piece moved down 2 rows
        assertFalse(updatedBoard.get(Game.HEIGHT - 4).get(Game.WIDTH - 1));
        assertFalse(updatedBoard.get(Game.HEIGHT - 3).get(Game.WIDTH - 1));
        assertTrue(updatedBoard.get(Game.HEIGHT - 2).get(Game.WIDTH - 1));
        assertTrue(updatedBoard.get(Game.HEIGHT - 1).get(Game.WIDTH - 1));
    }

    @Test
    public void testUpdateTripleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH - 1; i++) {
            riggedBoard.get(Game.HEIGHT - 3).set(i, true);
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now put this "I" piece in the bottom right corner, standing upright.
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
        testGame2.update();

        List<ArrayList<Boolean>> updatedBoard = testGame2.getBoard();
        assertEquals(Game.TRIPLE_POINTS, testGame2.getScore());
        assertEquals(3, testGame2.getLinesCleared());
        for (int c = 0; c < Game.WIDTH - 1; c++) {
            assertFalse(updatedBoard.get(Game.HEIGHT - 3).get(c));
            assertFalse(updatedBoard.get(Game.HEIGHT - 2).get(c));
            assertFalse(updatedBoard.get(Game.HEIGHT - 1).get(c));
        }

        // Check that the "I" piece moved down 3 rows
        for (int r = Game.HEIGHT - 4; r <= Game.HEIGHT - 2; r++) {
            assertFalse(updatedBoard.get(r).get(Game.WIDTH - 1));
        }
        assertTrue(updatedBoard.get(Game.HEIGHT - 1).get(Game.WIDTH - 1));
    }

    @Test
    public void testUpdateTetrisLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH - 1; i++) {
            riggedBoard.get(Game.HEIGHT - 4).set(i, true);
            riggedBoard.get(Game.HEIGHT - 3).set(i, true);
            riggedBoard.get(Game.HEIGHT - 2).set(i, true);
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now put this "I" piece in the bottom right corner, standing upright.
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
        testGame2.update();

        List<ArrayList<Boolean>> updatedBoard = testGame2.getBoard();
        assertEquals(Game.TETRIS_POINTS, testGame2.getScore());
        assertEquals(4, testGame2.getLinesCleared());

        // The bottommost four rows of the board should be empty
        for (int r = Game.HEIGHT - 4; r < Game.HEIGHT; r++) {
            for (int c = 0; c < Game.WIDTH; c++) {
                assertFalse(updatedBoard.get(r).get(c));
            }
        }
    }

    @Test
    public void testUpdateLineClearsWithLinesAbove() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        // riggedBoard is set to a board that satisfies the following:
        // - The 2nd and 4th rows from the bottom are completely filled, save for
        //   the rightmost column.
        // - The leftmost 3 columns of the bottom 5 rows are filled.
        // - There is a single tile in the 5th column of the 6th row from the bottom.
        // - All other cells are empty.
        for (int c = 0; c < Game.WIDTH - 1; c++) {
            riggedBoard.get(Game.HEIGHT - 2).set(c, true);
            riggedBoard.get(Game.HEIGHT - 4).set(c, true);
        }
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < Game.HEIGHT; r++) {
                if (r >= Game.HEIGHT - 5) {
                    riggedBoard.get(r).set(c, true);
                }
            }
        }
        riggedBoard.get(Game.HEIGHT - 6).set(5, true);

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now put this "I" piece in the bottom right corner, standing upright.
        // The 2nd and 4th rows from the bottom get completely filled, and
        // the 1st and 3rd rows from the bottom now have a tile in the rightmost column.
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
        testGame2.update();

        List<ArrayList<Boolean>> updatedBoard = testGame2.getBoard();
        assertEquals(Game.DOUBLE_POINTS, testGame2.getScore());
        assertEquals(2, testGame2.getLinesCleared());

        // The leftmost 3 columns of the bottom 3 rows should be filled.
        for (int c = 0; c < 3; c++) {
            for (int r = Game.HEIGHT - 3; r < Game.HEIGHT; r++) {
                assertTrue(updatedBoard.get(r).get(c));
            }
        }

        // The single tile from the 5th column of the 6th row from the bottom
        // should have moved down by 2 rows.
        assertTrue(updatedBoard.get(Game.HEIGHT - 4).get(5));
        assertFalse(updatedBoard.get(Game.HEIGHT - 6).get(5));

        // Check that the remaining tiles of the "I" piece are in the correct place.
        assertFalse(updatedBoard.get(Game.HEIGHT - 4).get(Game.WIDTH - 1));
        assertFalse(updatedBoard.get(Game.HEIGHT - 3).get(Game.WIDTH - 1));
        assertTrue(updatedBoard.get(Game.HEIGHT - 2).get(Game.WIDTH - 1));
        assertTrue(updatedBoard.get(Game.HEIGHT - 1).get(Game.WIDTH - 1));
    }

    @Test
    public void testUpdateGameOver() {
        List<ArrayList<Boolean>> board = testGame1.getBoard();
        for (int r = 2; r < Game.HEIGHT; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH; c++) {
                if (c == 0) {
                    row.add(false);
                } else {
                    row.add(true);
                }
            }
            board.set(r, row);
        }

        testGame1.update();

        // The S piece landed at the top, and the newly-spawned L piece intersects the S piece
        // Hence, the player tops out.
        assertTrue(testGame1.isGameOver());

        board = testGame1.getBoard();
        List<ArrayList<Boolean>> boardCopy = new ArrayList<ArrayList<Boolean>>();

        for (int i = 0; i < Game.HEIGHT; i++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int j = 0; j < Game.WIDTH; j++) {
                row.add(board.get(i).get(j));
            }
            boardCopy.add(row);
        }

        // Should do nothing
        testGame1.update();

        board = testGame1.getBoard();

        // Check that the board did not change
        for (int i = 0; i < Game.HEIGHT; i++) {
            for (int j = 0; j < Game.WIDTH; j++) {
                assertEquals(board.get(i).get(j), boardCopy.get(i).get(j));
            }
        }
    }

    @Test
    public void testUpdatePieceGeneration() {
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
            testGame1.update();

            Piece activePiece = testGame1.getActivePiece();
            if (!seenIPiece && activePiece instanceof IPiece) {
                seenIPiece = true;
            } else if (!seenJPiece && activePiece instanceof JPiece) {
                seenJPiece = true;
            } else if (!seenLPiece && activePiece instanceof LPiece) {
                seenLPiece = true;
            } else if (!seenOPiece && activePiece instanceof OPiece) {
                seenOPiece = true;
            } else if (!seenSPiece && activePiece instanceof SPiece) {
                seenSPiece = true;
            } else if (!seenTPiece && activePiece instanceof TPiece) {
                seenTPiece = true;
            } else if (!seenZPiece && activePiece instanceof ZPiece) {
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
    public void testSetBoardNoExceptionThrown() {
        try {
            testGame1.setBoard(Game.getBlankBoard());
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
        }

        assertTrue(boardsEqual(testGame1.getBoard(), Game.getBlankBoard()));
    }

    @Test
    public void testSetBoardIncorrectNumberOfRows() {
        List<ArrayList<Boolean>> previousBoard = testGame1.getBoard();
        List<ArrayList<Boolean>> badBoard = new ArrayList<ArrayList<Boolean>>();

        for (int r = 0; r < Game.HEIGHT - 1; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH; c++) {
                row.add(false);
            }
            badBoard.add(row);
        }

        try {
            testGame1.setBoard(badBoard);
            fail("IncorrectBoardSizeException should be thrown.");
        } catch (IncorrectBoardSizeException e) {
            // this is expected
        }

        List<ArrayList<Boolean>> currentBoard = testGame1.getBoard();
        assertTrue(boardsEqual(previousBoard, currentBoard));
    }

    @Test
    public void testSetBoardIncorrectNumberOfColumns() {
        List<ArrayList<Boolean>> previousBoard = testGame1.getBoard();
        List<ArrayList<Boolean>> badBoard = new ArrayList<ArrayList<Boolean>>();

        for (int r = 0; r < Game.HEIGHT; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH - 1; c++) {
                row.add(false);
            }
            badBoard.add(row);
        }

        try {
            testGame1.setBoard(badBoard);
            fail("IncorrectBoardSizeException should be thrown.");
        } catch (IncorrectBoardSizeException e) {
            // this is expected
        }

        List<ArrayList<Boolean>> currentBoard = testGame1.getBoard();
        assertTrue(boardsEqual(previousBoard, currentBoard));
    }

    @Test
    public void testSetBoardIncorrectNumberOfRowsAndColumns() {
        List<ArrayList<Boolean>> previousBoard = testGame1.getBoard();
        List<ArrayList<Boolean>> badBoard = new ArrayList<ArrayList<Boolean>>();

        for (int r = 0; r < Game.HEIGHT + 1; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH + 1; c++) {
                row.add(false);
            }
            badBoard.add(row);
        }

        try {
            testGame1.setBoard(badBoard);
            fail("IncorrectBoardSizeException should be thrown.");
        } catch (IncorrectBoardSizeException e) {
            // this is expected
        }

        List<ArrayList<Boolean>> currentBoard = testGame1.getBoard();
        assertTrue(boardsEqual(previousBoard, currentBoard));
    }

    @Test
    public void testSetBoardOneRowHasIncorrectNumberOfColumns() {
        List<ArrayList<Boolean>> previousBoard = testGame1.getBoard();
        List<ArrayList<Boolean>> badBoard = new ArrayList<ArrayList<Boolean>>();

        for (int r = 0; r < Game.HEIGHT - 1; r++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int c = 0; c < Game.WIDTH; c++) {
                row.add(false);
            }
            badBoard.add(row);
        }

        ArrayList<Boolean> lastRow = new ArrayList<Boolean>();
        for (int c = 0; c < Game.WIDTH + 1; c++) {
            lastRow.add(false);
        }
        badBoard.add(lastRow);

        try {
            testGame1.setBoard(badBoard);
            fail("IncorrectBoardSizeException should be thrown.");
        } catch (IncorrectBoardSizeException e) {
            // this is expected
        }

        List<ArrayList<Boolean>> currentBoard = testGame1.getBoard();
        assertTrue(boardsEqual(previousBoard, currentBoard));
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

    // EFFECTS: Ensures that the given board contains the given piece
    public static void checkBoardContainsPiece(List<ArrayList<Boolean>> boardCells, Piece piece) {
        for (Point p : piece.getTileLocations()) {
            assertTrue(boardCells.get(p.y).get(p.x));
        }
    }

    // EFFECTS: Ensures that the piece's tiles are located at the four given points
    public static void checkPieceHasTileLocations(Piece piece,
                                                  Point point1,
                                                  Point point2,
                                                  Point point3,
                                                  Point point4) {
        Set<Point> pieceTileLocations = piece.getTileLocations();
        assertTrue(pieceTileLocations.contains(point1));
        assertTrue(pieceTileLocations.contains(point2));
        assertTrue(pieceTileLocations.contains(point3));
        assertTrue(pieceTileLocations.contains(point4));
        assertEquals(4, pieceTileLocations.size());
    }

    // EFFECTS: returns the number of tiles on the game board
    private int getNumTilesOnBoard() {
        int numTiles = 0;
        for (List<Boolean> column : testGame1.getBoard()) {
            for (boolean cellOccupied : column) {
                if (cellOccupied) {
                    numTiles++;
                }
            }
        }
        return numTiles;
    }

    // EFFECTS: returns true if the two boards have the same number of rows and columns,
    //          and if all cells are equal. Returns false otherwise.
    private boolean boardsEqual(List<ArrayList<Boolean>> board1, List<ArrayList<Boolean>> board2) {
        if (board1.size() != board2.size()) {
            return false;
        }

        for (int r = 0; r < board1.size(); r++) {
            ArrayList<Boolean> rowFromBoard1 = board1.get(r);
            ArrayList<Boolean> rowFromBoard2 = board2.get(r);
            if (rowFromBoard1.size() != rowFromBoard2.size()) {
                return false;
            }
            for (int c = 0; c < rowFromBoard1.size(); c++) {
                if (rowFromBoard1.get(c) != rowFromBoard2.get(c)) {
                    return false;
                }
            }
        }

        return true;
    }
}