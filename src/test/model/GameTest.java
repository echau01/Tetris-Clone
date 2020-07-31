package model;

import exceptions.IllegalStartingLevelException;
import exceptions.IncorrectBoardSizeException;
import exceptions.NegativeLinesException;
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

    // Identical to testGame1, except that the first "I" piece is placed upright
    // at the bottom of the rightmost column.
    private Game testGame2;
    private static final int GAME_SEED = 5000;

    @BeforeEach
    public void setUp() {
        // First ten numbers that are produced by the random number generator with seed 5000:
        // 0, 1, 2, 4, 3, 6, 5, 2, 0, 4
        try {
            testGame1 = new Game(GAME_SEED, 0);
            testGame2 = new Game(GAME_SEED, 0);
        } catch (IllegalStartingLevelException e) {
            fail("IllegalStartingLevelException should not be thrown");
        }

        // The random number generator seed is set so that the first piece is an "I" piece.
        // We now set this "I" piece standing upright at the bottom of the rightmost column.
        // Calling update on testGame2 will spawn in the next piece (a "J" piece).
        Piece activePiece = testGame2.getActivePiece();
        activePiece.moveDown();
        activePiece.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            activePiece.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            activePiece.moveDown();
        }
    }

    @Test
    public void testConstructorStartingLevelZero() {
        assertEquals(0, testGame1.getScore());
        assertEquals(0, testGame1.getLinesCleared());
        assertFalse(testGame1.isGameOver());
        assertNotNull(testGame1.getActivePiece());
        assertNotNull(testGame1.getNextPiece());

        // The first piece is an "I" piece

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2) - 1;

        Point point1 = new Point(point1XPos, 0);
        Point point2 = new Point(point1XPos + 1, 0);
        Point point3 = new Point(point1XPos + 2, 0);
        Point point4 = new Point(point1XPos + 3, 0);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);

        // Check that the "I" piece shows up on the board
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        assertTrue(testGame1.getNextPiece() instanceof JPiece);
    }

    @Test
    public void testConstructorStartingLevelNoException() {
        try {
            Game myGame = new Game(0, 4);
            Game myGame2 = new Game(0, 18);
            assertEquals(4, myGame.getLevel());
            assertEquals(18, myGame2.getLevel());
        } catch (IllegalStartingLevelException e) {
            fail("IllegalStartingLevelException should not be thrown.");
        }
    }

    @Test
    public void testConstructorStartingLevelExceptionalCases() {
        try {
            Game myGame = new Game(0, -1);
            fail("IllegalStartingLevelException should be thrown.");
        } catch (IllegalStartingLevelException e) {
            // this is expected
        }

        try {
            Game myGame = new Game(0, 19);
            fail("IllegalStartingLevelException should be thrown.");
        } catch (IllegalStartingLevelException e) {
            // this is expected
        }
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
        // The first piece is an "I" piece.
        for (int i = 0; i <= Game.HEIGHT - 2; i++) {
            testGame1.update();
        }
        // The "I" piece should now be just about to land at the bottom
        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2) - 1;

        Point point1 = new Point(point1XPos, Game.HEIGHT - 1);
        Point point2 = new Point(point1XPos + 1, Game.HEIGHT - 1);
        Point point3 = new Point(point1XPos + 2, Game.HEIGHT - 1);
        Point point4 = new Point(point1XPos + 3, Game.HEIGHT - 1);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        // Update the game once more:

        testGame1.update();

        // At this point, the "I" piece should have landed at the bottom, and
        // the next piece (a "J" piece) will have just spawned in.

        point1.x = Math.floorDiv(Game.WIDTH - 1, 2);
        point1.y = 0;
        point2.x = point1.x + 1;
        point2.y = 0;
        point3.x = point1.x + 2;
        point3.y = 0;
        point4.x = point1.x + 2;
        point4.y = 1;

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        assertTrue(testGame1.getNextPiece() instanceof LPiece);
    }

    @Test
    public void testUpdatePieceLandsOnPiece() {
        // Get the "I" piece to land at the bottom, then get the "J" piece to
        // be just about to land on the "I" piece.
        for (int i = 1; i <= 2 * Game.HEIGHT - 3; i++) {
            testGame1.update();
        }

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, Game.HEIGHT - 3);
        Point point2 = new Point(point1XPos + 1, Game.HEIGHT - 3);
        Point point3 = new Point(point1XPos + 2, Game.HEIGHT - 3);
        Point point4 = new Point(point1XPos + 2, Game.HEIGHT - 2);

        checkPieceHasTileLocations(testGame1.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(testGame1.getBoard(), testGame1.getActivePiece());

        // Update the game once more, causing the "J" piece to land on the "I" piece:

        testGame1.update();

        // The "L" piece should have spawned in now.

        point1.x = point1XPos;
        point1.y = 1;
        point2.x = point1.x;
        point2.y = 0;
        point3.x = point1.x + 1;
        point3.y = 0;
        point4.x = point1.x + 2;
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

        // Fill in the tiles occupied by the upright "I" piece in the bottom right corner
        for (int i = Game.HEIGHT - 4; i <= Game.HEIGHT - 1; i++) {
            riggedBoard.get(i).set(Game.WIDTH - 1, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
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

        // Fill in the tiles occupied by the upright "I" piece in the bottom right corner
        for (int i = Game.HEIGHT - 4; i <= Game.HEIGHT - 1; i++) {
            riggedBoard.get(i).set(Game.WIDTH - 1, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
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

        // Fill in the tiles occupied by the upright "I" piece in the bottom right corner
        for (int i = Game.HEIGHT - 4; i <= Game.HEIGHT - 1; i++) {
            riggedBoard.get(i).set(Game.WIDTH - 1, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
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

        // Fill in the tiles occupied by the upright "I" piece in the bottom right corner
        for (int i = Game.HEIGHT - 4; i <= Game.HEIGHT - 1; i++) {
            riggedBoard.get(i).set(Game.WIDTH - 1, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
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

        // First, we set riggedBoard to be a board that satisfies the following:
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

        // Now we fill in the tiles occupied by the upright "I" piece in the bottom right corner.
        // As a result, the 2nd and 4th rows from the bottom get completely filled, and
        // the 1st and 3rd rows from the bottom now have a tile in the rightmost column.
        for (int i = Game.HEIGHT - 4; i <= Game.HEIGHT - 1; i++) {
            riggedBoard.get(i).set(Game.WIDTH - 1, true);
        }

        try {
            testGame2.setBoard(riggedBoard);
        } catch (IncorrectBoardSizeException e) {
            fail("IncorrectBoardSizeException was incorrectly thrown.");
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

        // For every row of the board except the top row, fill every column except
        // the leftmost column with tiles. We don't fill the leftmost column because
        // we want to avoid line clears.
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

        // Updating the game now will cause the "I" piece to move down one row.
        // The subsequent update will cause the "I" piece to land on the second-highest
        // row and the "J" piece to spawn. The "J" piece will intersect the "I" piece
        // and cause the player to top out.
        testGame1.update();
        assertFalse(testGame1.isGameOver());

        testGame1.update();
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

        // We will have seen all the different piece types after 7 * Game.HEIGHT game updates.
        for (int i = 0; i < 7 * Game.HEIGHT; i++) {
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

        assertTrue(listsOfArrayListsEqual(testGame1.getBoard(), Game.getBlankBoard()));
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
        assertTrue(listsOfArrayListsEqual(previousBoard, currentBoard));
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
        assertTrue(listsOfArrayListsEqual(previousBoard, currentBoard));
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
        assertTrue(listsOfArrayListsEqual(previousBoard, currentBoard));
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
        assertTrue(listsOfArrayListsEqual(previousBoard, currentBoard));
    }

    @Test
    public void testSetLinesClearedPositiveLines() {
        Game myGame = new Game(0, 0);

        try {
            myGame.setLinesCleared(100);
        } catch (NegativeLinesException e) {
            fail("NegativeLinesException should not be thrown.");
        }

        assertEquals(100, myGame.getLinesCleared());
        assertEquals(10, myGame.getLevel());
        assertEquals(0, myGame.getScore());
    }

    @Test
    public void testSetLinesClearedZeroLines() {
        Game myGame = new Game(0, 0);

        try {
            myGame.setLinesCleared(0);
        } catch (NegativeLinesException e) {
            fail("NegativeLinesException should not be thrown.");
        }

        assertEquals(0, myGame.getLinesCleared());
        assertEquals(0, myGame.getLevel());
        assertEquals(0, myGame.getScore());
    }

    @Test
    public void testSetLinesClearedNegativeLines() {
        Game myGame = new Game(0, 0);

        try {
            myGame.setLinesCleared(-100);
            fail("NegativeLinesException should be thrown.");
        } catch (NegativeLinesException e) {
            // this is expected
        }

        assertEquals(0, myGame.getLinesCleared());
        assertEquals(0, myGame.getLevel());
        assertEquals(0, myGame.getScore());
    }

    @Test
    public void testGetLevelStartingLevelZero() {
        Game myGame = new Game(0, 0);
        myGame.setLinesCleared(129);
        assertEquals(12, myGame.getLevel());

        myGame.setLinesCleared(90);
        assertEquals(9, myGame.getLevel());
    }

    @Test
    public void testGetLevelStartingLevelNotZero() {
        // myGame has starting level 8 and will increase to level 9 at 90 lines
        Game myGame = new Game(0, 8);
        myGame.setLinesCleared(10);
        assertEquals(8, myGame.getLevel());

        myGame.setLinesCleared(89);
        assertEquals(8, myGame.getLevel());

        myGame.setLinesCleared(90);
        assertEquals(9, myGame.getLevel());

        myGame.setLinesCleared(100);
        assertEquals(10, myGame.getLevel());

        // myGame2 has starting level 12 and will increase to level 13 at 100 lines
        Game myGame2 = new Game(0, 12);
        myGame2.setLinesCleared(30);
        assertEquals(12, myGame2.getLevel());

        myGame2.setLinesCleared(99);
        assertEquals(12, myGame2.getLevel());

        myGame2.setLinesCleared(100);
        assertEquals(13, myGame2.getLevel());

        myGame2.setLinesCleared(140);
        assertEquals(17, myGame2.getLevel());

        // myGame2 has starting level 18 and will increase to level 19 at 130 lines
        Game myGame3 = new Game(0, 18);
        myGame2.setLinesCleared(30);
        assertEquals(18, myGame3.getLevel());

        myGame3.setLinesCleared(129);
        assertEquals(18, myGame3.getLevel());

        myGame3.setLinesCleared(130);
        assertEquals(19, myGame3.getLevel());

        myGame3.setLinesCleared(140);
        assertEquals(20, myGame3.getLevel());
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

    // EFFECTS: returns true if list1 and list2 have the same size, and if each ArrayList in
    //          list1 is equal to the ArrayList at the same index in list2. Returns false otherwise.
    //
    //          Two arraylists are equal if and only if they have the same size and contain equal
    //          elements at equal indices.
    public static boolean listsOfArrayListsEqual(List<ArrayList<Boolean>> list1, List<ArrayList<Boolean>> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int r = 0; r < list1.size(); r++) {
            ArrayList<Boolean> rowFromBoard1 = list1.get(r);
            ArrayList<Boolean> rowFromBoard2 = list2.get(r);
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
}