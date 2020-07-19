package model;

import model.pieces.Tetromino;

import java.util.ArrayList;
import java.util.List;

// Represents a Tetris game
public class Game {
    // Width and height of the board
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    // Points awarded for line clears
    public static final int SINGLE_POINTS = 40;
    public static final int DOUBLE_POINTS = 100;
    public static final int TRIPLE_POINTS = 300;
    public static final int TETRIS_POINTS = 1200;

    // Used with the random number generator that chooses which type of tetromino to make next.
    // Credit to https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html for the tutorial
    // on how to make enums.
    private enum TetrominoEnum {
        IPIECE(0),
        JPIECE(1),
        LPIECE(2),
        OPIECE(3),
        SPIECE(4),
        TPIECE(5),
        ZPIECE(6);

        int rngNumber;

        TetrominoEnum(int rngNumber) {
            this.rngNumber = rngNumber;
        }
    }

    // MODIFIES: this
    // EFFECTS: Starts a new game. Tetrominoes are randomly generated using the given seed.
    //          Randomly generates an active tetromino and a next tetromino. The active tetromino
    //          spawns at the top of the board.
    public void startNewGame(long seed) {
        // Note: this method has a "seed" argument just so we can test the Game class
        // without the randomness of the tetromino generator. I got this idea from the following answer
        // on StackOverflow by the user Parappa: https://stackoverflow.com/a/88110/3335320

        //stub
    }

    // MODIFIES: this
    // EFFECTS: updates the state of the game.
    //          Moves the active tetromino down one row if there is space.
    //          Otherwise, if the game is not over, clears any filled rows and awards points accordingly.
    //          Then, begins dropping a new tetromino from the top of the board.
    //          If the game is over, ends the game.
    //          Note: if the game is already over, calling this method does nothing.
    public void update() {
        //stub
    }

    // REQUIRES: list contains Game.WIDTH arraylists, each of which contain
    //           Game.HEIGHT booleans
    // MODIFIES: this
    // EFFECTS: sets the board of this game
    public void setBoard(List<ArrayList<Boolean>> board) {
        //stub
    }

    // EFFECTS: returns the board, represented as a list of arraylists of booleans
    //          The r'th element in the c'th arraylist is true if the cell at coordinates
    //          (c, r) is occupied by a tile, and false otherwise (where r and c are indexed
    //          starting from 0).
    public List<ArrayList<Boolean>> getBoard() {
        return null;    //stub
    }

    // EFFECTS: returns the active tetromino
    public Tetromino getActiveTetromino() {
        return null;    //stub
    }

    // EFFECTS: returns the next tetromino
    public Tetromino getNextTetromino() {
        return null;    //stub
    }

    // EFFECTS: returns the player's score
    public int getScore() {
        return 0;   //stub
    }

    // EFFECTS: returns the number of lines cleared so far
    public int getLinesCleared() {
        return 0;   //stub
    }

    // EFFECTS: returns true if the game is over, false otherwise
    public boolean isGameOver() {
        return false;   //stub
    }
}
