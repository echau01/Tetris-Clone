package ui;

import model.Game;
import model.pieces.Piece;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Represents the panel that displays the Tetris board.
public class BoardPanel extends JPanel implements Observer {
    // Side length of a tetromino tile, in pixels
    public static final int TILE_SIDE_LENGTH = 30;

    public static final int WIDTH = Game.WIDTH * TILE_SIDE_LENGTH;
    public static final int HEIGHT = Game.HEIGHT * TILE_SIDE_LENGTH;

    private Game game;

    // The timer that advances the game forward at set intervals
    private Timer timer;

    // EFFECTS: creates a new BoardPanel to display the given game
    public BoardPanel(Game game) {
        this.game = game;
        game.addObserver(this);

        // I learned about the difference between setSize and setPreferredSize from StackOverflow.
        // https://stackoverflow.com/questions/1783793/java-difference-between-the-setpreferredsize-and-setsize-methods-in-compone
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        initTimer();

        setBackground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: handles a key press given the key code.
    //          If the up arrow key is pressed, rotates the active piece 90 degrees clockwise.
    //          If the down arrow key is pressed, moves the active piece down one tile.
    //          If the left arrow key is pressed, moves the active piece left.
    //          If the right arrow key is pressed, moves the active piece right.
    //          If the space key is pressed, hard drops the active piece.
    public void handleKeyPressed(int keyCode) {
        // Adapted from the keyPressed method in the SIGame class in the SpaceInvaders repository
        // https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase/blob/master/src/main/ca/ubc/cpsc210/spaceinvaders/model/SIGame.java
        Piece activePiece = game.getActivePiece();

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_KP_UP) {
            activePiece.rotate();
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_KP_DOWN) {
            activePiece.moveDown();
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT) {
            activePiece.moveLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT) {
            activePiece.moveRight();
        } else if (keyCode == KeyEvent.VK_SPACE) {
            hardDropActivePiece();
        }

        repaint();
    }

    // EFFECTS: paints the board panel. The board appears as a grid, with the occupied
    //          cells filled in. An occupied cell is coloured red if it holds a tile of
    //          the active piece. Otherwise, an occupied cell is coloured black.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<ArrayList<Boolean>> board = game.getBoard();

        // https://stackoverflow.com/questions/34036216/drawing-java-grid-using-swing taught me how
        // to draw a grid of rectangles.
        for (int r = 0; r < board.size(); r++) {
            ArrayList<Boolean> row = board.get(r);
            for (int c = 0; c < row.size(); c++) {
                if (cellShouldBeFilledWithColour(r, c)) {
                    drawColouredCell(g, r, c);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRect(c * TILE_SIDE_LENGTH, r * TILE_SIDE_LENGTH, TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if observable is an instance of Game, then updates the timer's delay between ticks
    //          to reflect changes in the game's level. The arg parameter is ignored.
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            timer.setDelay(getMillisecondsPerUpdate());
        }
    }

    // MODIFIES: this
    // EFFECTS: immediately drops the active piece in the game straight down as far as it can go.
    //          In Tetris, we call this action a "hard drop".
    private void hardDropActivePiece() {
        Piece activePiece = game.getActivePiece();
        boolean movedDown = activePiece.moveDown();
        while (movedDown) {
            movedDown = activePiece.moveDown();
        }
        game.update();
    }

    // MODIFIES: this
    // EFFECTS: initializes the timer that advances the game at set intervals.
    private void initTimer() {
        // The following webpage taught me how to make a Timer:
        // https://docs.oracle.com/javase/7/docs/api/javax/swing/Timer.html
        ActionListener gameUpdater = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.update();
                repaint();
            }
        };

        timer = new Timer(getMillisecondsPerUpdate(), gameUpdater);
        timer.start();
    }

    // EFFECTS: returns true if the cell at the given row and column is occupied by a tile, or is the
    //          location of a point in game.getActivePiece().getHardDropTileLocations(). Returns false otherwise.
    private boolean cellShouldBeFilledWithColour(int row, int column) {
        return game.getBoard().get(row).get(column)
                || game.getActivePiece().getHardDropTileLocations().contains(new Point(column, row));
    }

    // EFFECTS: draws the cell at the given row and column, assuming that the cell should be filled with colour.
    //          This method has no effect otherwise.
    private void drawColouredCell(Graphics g, int row, int column) {
        if (!cellShouldBeFilledWithColour(row, column)) {
            return;
        }

        Set<Point> activeTileLocations = game.getActivePiece().getTileLocations();
        Set<Point> hardDropTileLocations = game.getActivePiece().getHardDropTileLocations();

        if (activeTileLocations.contains(new Point(column, row))) {
            g.setColor(Color.RED);
        } else if (hardDropTileLocations.contains(new Point(column, row))) {
            // https://stackoverflow.com/a/8111007/3335320 taught me how to decrease a colour's opacity.
            g.setColor(new Color(Color.RED.getRed(), Color.RED.getBlue(), Color.RED.getGreen(), 127));
        } else {
            g.setColor(Color.BLACK);
        }

        g.fillRect(column * TILE_SIDE_LENGTH, row * TILE_SIDE_LENGTH, TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
        if (g.getColor() == Color.BLACK) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawRect(column * TILE_SIDE_LENGTH, row * TILE_SIDE_LENGTH, TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
    }

    // EFFECTS: returns the number of milliseconds between consecutive game updates.
    //          We will refer to this number as MSPU (milliseconds per update). Generally,
    //          as the game level increases, the MSPU decreases in order to make pieces
    //          fall faster. The MSPU decreases linearly from levels 0 to 18, is held constant
    //          between levels 19 and 28, and reaches its lowest value at level 29.
    //
    //          The MSPU values are very loosely based on the "frames per gridcell" values given
    //          in the Details section of the following webpage: https://tetris.wiki/Tetris_(NES,_Nintendo)
    private int getMillisecondsPerUpdate() {
        int level = game.getLevel();
        if (0 <= level && level <= 18) {
            return 1000 - 52 * level;
        } else if (19 <= level && level <= 28) {
            return 40;
        } else {
            return 20;
        }
    }
}
