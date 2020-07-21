package test.model;

import model.Game;
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
        assertNotNull(g.getActivePiece());
        assertNotNull(g.getNextPiece());

        // The first piece is an S piece

        int point1XPos = Math.floorDiv(Game.WIDTH - 1, 2);

        Point point1 = new Point(point1XPos, 1);
        Point point2 = new Point(point1XPos + 1, 1);
        Point point3 = new Point(point1XPos + 1, 0);
        Point point4 = new Point(point1XPos + 2, 0);

        checkPieceHasTileLocations(g.getActivePiece(), point1, point2, point3, point4);

        // Check that the S piece shows up on the board
        checkBoardContainsPiece(g.getBoard(), g.getActivePiece());

        assertTrue(g.getNextPiece() instanceof LPiece);
    }

    @Test
    public void testUpdateMoveDownOnce() {
        // We first make a copy of the current set of tile locations for the active piece.
        // We must make this copy because we do not want oldTileLocations to change
        // when we call g.update().
        Set<Point> oldTileLocations = new HashSet<Point>();
        for (Point location : g.getActivePiece().getTileLocations()) {
            oldTileLocations.add(new Point(location));
        }

        g.update();

        Set<Point> newTileLocations = g.getActivePiece().getTileLocations();

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
        List<ArrayList<Boolean>> board = g.getBoard();
        checkBoardContainsPiece(board, g.getActivePiece());

        // Check that there are only four tiles on the board:
        assertEquals(4, getNumTilesOnBoard());
    }

    @Test
    public void testUpdatePieceLandsAtBottom() {
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

        checkPieceHasTileLocations(g.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(g.getBoard(), g.getActivePiece());

        // Update the game once more:

        g.update();

        // At this point, the S piece should have landed at the bottom, and
        // the next piece (an L piece) will have just spawned in.

        point2.x = point1XPos;
        point1.y = 1;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkPieceHasTileLocations(g.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(g.getBoard(), g.getActivePiece());

        assertTrue(g.getNextPiece() instanceof IPiece);
    }

    @Test
    public void testUpdatePieceLandsOnPiece() {
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

        checkPieceHasTileLocations(g.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(g.getBoard(), g.getActivePiece());

        // Update the game once more, causing the L piece to land on the S piece:

        g.update();

        // The "I" piece should have spawned in now.

        point1.x = point1XPos - 1;
        point1.y = 0;
        point2.y = 0;
        point3.y = 0;
        point4.y = 0;

        checkPieceHasTileLocations(g.getActivePiece(), point1, point2, point3, point4);
        checkBoardContainsPiece(g.getBoard(), g.getActivePiece());
    }

    @Test
    public void testUpdateSingleLineClear() {
        List<ArrayList<Boolean>> riggedBoard = Game.getBlankBoard();

        for (int i = 0; i < Game.WIDTH - 1; i++) {
            riggedBoard.get(Game.HEIGHT - 1).set(i, true);
        }

        g.setBoard(riggedBoard);
        g.setActivePiece(makeUprightIPieceInBottomRightCorner());
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.SINGLE_POINTS, g.getScore());
        assertEquals(1, g.getLinesCleared());
        for (int c = 0; c <= Game.WIDTH - 2; c++) {
            assertFalse(updatedBoard.get(Game.HEIGHT - 1).get(c));
        }

        // Check that the I piece moved down 1 row
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

        g.setBoard(riggedBoard);
        g.setActivePiece(makeUprightIPieceInBottomRightCorner());
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.DOUBLE_POINTS, g.getScore());
        assertEquals(2, g.getLinesCleared());
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

        g.setBoard(riggedBoard);
        g.setActivePiece(makeUprightIPieceInBottomRightCorner());
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.TRIPLE_POINTS, g.getScore());
        assertEquals(3, g.getLinesCleared());
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

        g.setBoard(riggedBoard);
        g.setActivePiece(makeUprightIPieceInBottomRightCorner());
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.TETRIS_POINTS, g.getScore());
        assertEquals(4, g.getLinesCleared());

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

        g.setBoard(riggedBoard);

        // Now, we put an upright "I" piece in the bottom right corner.
        // The 2nd and 4th rows from the bottom get completely filled, and
        // the 1st and 3rd rows from the bottom now have a tile in the rightmost column.
        g.setActivePiece(makeUprightIPieceInBottomRightCorner());
        g.update();

        List<ArrayList<Boolean>> updatedBoard = g.getBoard();
        assertEquals(Game.DOUBLE_POINTS, g.getScore());
        assertEquals(2, g.getLinesCleared());

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
        List<ArrayList<Boolean>> board = g.getBoard();
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

        g.update();

        // The S piece landed at the top, and the newly-spawned L piece intersects the S piece
        // Hence, the player tops out.
        assertTrue(g.isGameOver());

        board = g.getBoard();
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
            g.update();

            Piece activePiece = g.getActivePiece();
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
        for (List<Boolean> column : g.getBoard()) {
            for (boolean cellOccupied : column) {
                if (cellOccupied) {
                    numTiles++;
                }
            }
        }
        return numTiles;
    }

    // REQUIRES: the first four rows of g's board, as well as the rightmost column of the board,
    //           have no tiles.
    // EFFECTS: returns an upright "I" piece that is located in the bottom right corner of Game g's board
    private IPiece makeUprightIPieceInBottomRightCorner() {
        IPiece ip = new IPiece(g);
        ip.moveDown();
        ip.rotate();
        for (int i = 0; i < Game.WIDTH; i++) {
            ip.moveRight();
        }
        for (int i = 0; i < Game.HEIGHT; i++) {
            ip.moveDown();
        }
        return ip;
    }
}