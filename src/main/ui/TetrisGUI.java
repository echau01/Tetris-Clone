package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

// Represents the main GUI window.
public class TetrisGUI extends JFrame implements Observer {
    /* Code adapted from SimpleDrawingPlayer-Complete's DrawingEditor class.
     * https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Complete/blob/master/src/ui/DrawingEditor.java
     */

    private Game game;
    private Random random;
    private BoardPanel boardPanel;
    private GameInfoPanel gameInfoPanel;

    // EFFECTS: makes a new Tetris GUI window
    public TetrisGUI() {
        super("Tetris");
        initFields();
        initGraphics();

        // The following sources helped me create this key listener:
        // -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
        // -> https://stackoverflow.com/a/8961998/3335320
        // -> The SpaceInvaders class from the SpaceInvaders repository
        //    https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase/blob/master/src/main/ca/ubc/cpsc210/spaceinvaders/ui/SpaceInvaders.java
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boardPanel.handleKeyPressed(e.getKeyCode());
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: if observable is an instance of Game, checks to see if the game is over.
    //          If the game is over, then creates and shows a dialog that:
    //           - tells the user that the game is over
    //           - shows the user their final score and number of lines cleared
    //           - displays buttons that the user can press to indicate their next action
    //          The arg parameter is ignored.
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            Game observedGame = (Game) observable;

            if (observedGame.isGameOver()) {
                makeGameOverDialog();
            }
        }
    }

    // EFFECTS: returns the game instance running in this TetrisGUI
    public Game getGame() {
        return game;
    }

    // MODIFIES: this
    // EFFECTS: initializes all the fields of this GUI to their default values
    private void initFields() {
        random = new Random();
        game = new Game(random.nextInt(), 0);
        boardPanel = new BoardPanel(game);
        gameInfoPanel = new GameInfoPanel(game);
        game.addObserver(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes graphics-related properties of the GUI window.
    private void initGraphics() {
        setLayout(new FlowLayout());
        add(boardPanel);
        add(gameInfoPanel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // EFFECTS: creates and shows a dialog that:
    //          - tells the user that the game is over
    //          - shows the user their final score and number of lines cleared
    //          - displays buttons that the user can press to indicate their next action
    private void makeGameOverDialog() {
        // TODO

    }
}
