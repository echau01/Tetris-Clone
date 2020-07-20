package model;

import model.pieces.*;

import java.awt.*;
import java.util.*;
import java.util.List;

// Represents a Tetris game
public class Game {
    public static final int NUM_TETRIS_PIECES = 7;

    // Width and height of the board
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    // Points awarded for line clears
    public static final int SINGLE_POINTS = 40;
    public static final int DOUBLE_POINTS = 100;
    public static final int TRIPLE_POINTS = 300;
    public static final int TETRIS_POINTS = 1200;

    private Random random;
    private Piece activePiece;
    private Piece nextPiece;
    private List<ArrayList<Boolean>> board;
    private int score;
    private int linesCleared;
    private boolean gameOver;

    // Used with the random number generator that chooses which type of piece to make next.
    // Credit to https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html for the tutorial
    // on how to make enums.
    private enum PieceEnum {
        IPIECE(0),
        JPIECE(1),
        LPIECE(2),
        OPIECE(3),
        SPIECE(4),
        TPIECE(5),
        ZPIECE(6);

        int value;

        PieceEnum(int value) {
            this.value = value;
        }
    }

    // MODIFIES: this
    // EFFECTS: Starts a new game. Pieces are randomly generated using the given seed.
    //          Randomly generates an active piece and a next piece. The active piece
    //          spawns at the top of the board.
    public void startNewGame(long seed) {
        // Note: this method has a "seed" argument just so we can test the Game class
        // without the randomness of the tetromino generator. I got this idea from the following answer
        // on StackOverflow by the user Parappa: https://stackoverflow.com/a/88110/3335320

        random = new Random(seed);
        activePiece = intToPiece(random.nextInt(NUM_TETRIS_PIECES));
        nextPiece = intToPiece(random.nextInt(NUM_TETRIS_PIECES));
        board = getBlankBoard();
        score = 0;
        linesCleared = 0;
        gameOver = false;

        addPieceToBoard(activePiece);
    }

    // MODIFIES: this
    // EFFECTS: updates the state of the game.
    //          Moves the active piece down one row if there is space.
    //          Otherwise, if the game is not over, clears any filled rows and awards points accordingly.
    //          Then, begins dropping a new piece from the top of the board.
    //          If the game is over (because the player topped out), ends the game.
    //          Note: if the game is already over, calling this method does nothing.
    public void update() {
        if (!gameOver) {
            boolean pieceMovedDown = activePiece.moveDown();
            if (!pieceMovedDown) {
                clearLines();
                activePiece = nextPiece;
                if (!addPieceToBoard(activePiece)) {
                    gameOver = true;
                }
                nextPiece = intToPiece(random.nextInt(NUM_TETRIS_PIECES));
            }
        }
    }

    // REQUIRES: board contains Game.HEIGHT arraylists, each of which contain Game.WIDTH booleans
    // MODIFIES: this
    // EFFECTS: sets the board of this game
    public void setBoard(List<ArrayList<Boolean>> board) {
        this.board = board;
    }

    // MODIFIES: this
    // EFFECTS: sets the active piece of the game
    public void setActivePiece(Piece piece) {
        this.activePiece = piece;
    }

    // EFFECTS: returns the board, represented as a list of arraylists of booleans
    //          The c'th element in the r'th arraylist is true if the cell at coordinates
    //          (c, r) is occupied by a tile, and false otherwise (where c and r are indexed
    //          starting from 0).
    public List<ArrayList<Boolean>> getBoard() {
        return board;
    }

    // EFFECTS: returns the active piece
    public Piece getActivePiece() {
        return activePiece;
    }

    // EFFECTS: returns the next piece
    public Piece getNextPiece() {
        return nextPiece;    //stub
    }

    // EFFECTS: returns the player's score
    public int getScore() {
        return score;   //stub
    }

    // EFFECTS: returns the number of lines cleared so far
    public int getLinesCleared() {
        return linesCleared;   //stub
    }

    // EFFECTS: returns true if the game is over, false otherwise
    public boolean isGameOver() {
        return gameOver;
    }

    // EFFECTS: returns a blank game board
    public static List<ArrayList<Boolean>> getBlankBoard() {
        List<ArrayList<Boolean>> blankBoard = new ArrayList<ArrayList<Boolean>>();

        for (int i = 0; i < Game.HEIGHT; i++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int j = 0; j < Game.WIDTH; j++) {
                row.add(false);
            }
            blankBoard.add(row);
        }
        return blankBoard;
    }

    // REQUIRES: 0 <= num <= PieceEnum.values.length - 1
    // EFFECTS: returns a new piece associated with given number (as specified
    //          in PieceEnum).
    private Piece intToPiece(int num) {
        if (num == PieceEnum.IPIECE.value) {
            return new IPiece(this);
        } else if (num == PieceEnum.JPIECE.value) {
            return new JPiece(this);
        } else if (num == PieceEnum.LPIECE.value) {
            return new LPiece(this);
        } else if (num == PieceEnum.OPIECE.value) {
            return new OPiece(this);
        } else if (num == PieceEnum.SPIECE.value) {
            return new SPiece(this);
        } else if (num == PieceEnum.TPIECE.value) {
            return new TPiece(this);
        } else {
            return new ZPiece(this);
        }
    }

    // REQUIRES: the tile locations of piece are all on the board (not out of bounds).
    // MODIFIES: this
    // EFFECTS: adds the given piece to the board. If the addition does not cause the piece to
    //          intersect with other tiles, returns true. If the piece intersects with other tiles on the
    //          board, returns false.
    private boolean addPieceToBoard(Piece piece) {
        boolean noIntersection = true;
        for (Point p : piece.getTileLocations()) {
            // The addPieceToBoard method is only called when spawning a new piece into the board.
            // The only reason a piece could fail to spawn is if it is forced to intersect
            // with a tile.
            if (board.get(p.y).get(p.x)) {
                noIntersection = false;
            }
            board.get(p.y).set(p.x, true);
        }
        return noIntersection;
    }

    // MODIFIES: this
    // EFFECTS: clears any filled rows and moves the tiles in above rows downward
    //          by the appropriate number of rows. In particular, if n rows below a particular
    //          row, R, are cleared, then R will be moved down n rows.
    //          Changes the number of lines cleared and the player's score accordingly.
    private void clearLines() {
        List<Integer> rowsCleared = getFilledRowsIndices();

        for (int i = 0; i < rowsCleared.size(); i++) {
            ArrayList<Boolean> blankRow = new ArrayList<Boolean>();
            for (int j = 0; j < Game.WIDTH; j++) {
                blankRow.add(false);
            }
            board.remove((int) rowsCleared.get(i));
            board.add(0, blankRow);
        }

        int numRowsCleared = rowsCleared.size();
        linesCleared += numRowsCleared;
        if (numRowsCleared == 1) {
            score += SINGLE_POINTS;
        } else if (numRowsCleared == 2) {
            score += DOUBLE_POINTS;
        } else if (numRowsCleared == 3) {
            score += TRIPLE_POINTS;
        } else if (numRowsCleared == 4) {
            score += TETRIS_POINTS;
        }
    }

    // EFFECTS: returns a list of indices of rows that are currently completely
    //          filled with tiles
    private List<Integer> getFilledRowsIndices() {
        List<Integer> rowsCleared = new ArrayList<Integer>();

        for (int i = 0; i < Game.HEIGHT; i++) {
            List<Boolean> row = board.get(i);
            boolean rowCompletelyFilled = true;
            for (Boolean cellOccupied : row) {
                if (!cellOccupied) {
                    rowCompletelyFilled = false;
                    break;
                }
            }
            if (rowCompletelyFilled) {
                rowsCleared.add(i);
            }
        }

        return rowsCleared;
    }
}
