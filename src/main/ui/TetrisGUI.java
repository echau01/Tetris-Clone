package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
    private TemporaryScoreboardManager tempScoreboardManager = TemporaryScoreboardManager.getInstance();

    // EFFECTS: makes a new Tetris GUI window and starts a new Tetris game
    public TetrisGUI() {
        super("Tetris");
        startNewGame();

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

        setUpClosingBehaviour();
    }

    // MODIFIES: this
    // EFFECTS: tells the window what to do when the user tries to close the window with the "X" button.
    //          The user should be prompted to save their unsaved scoreboard entries upon closing the window.
    private void setUpClosingBehaviour() {
        // The following code, which prompts the user to save their temporary scoreboard before exiting
        // the program, is adapted from this StackOverflow post: https://stackoverflow.com/a/34039602/3335320
        // In addition, https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html taught me
        // how to make dialog popup windows.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (tempScoreboardManager.getTempScoreboardSize() != 0) {
                    if (JOptionPane.showConfirmDialog(null,
                            "You have unsaved scoreboard entries. Do you want to permanently save them to file?",
                            "Save Scoreboard", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try {
                            tempScoreboardManager.saveTempScoreboard();
                            JOptionPane.showMessageDialog(null,
                                    "Successfully saved scoreboard entries to file.");
                        } catch (IOException ioException) {
                            JOptionPane.showMessageDialog(null,
                                    "Could not save scoreboard entries to file!",
                                    "An Error Occurred", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                TetrisGUI.this.dispose();
                System.exit(0);
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
                new GameOverDialog(observedGame, this);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: starts a new Tetris game
    public void startNewGame() {
        // https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java taught me
        // how to remove all components from the window
        this.getContentPane().removeAll();
        initFields();
        initGraphics();
        this.repaint();
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
}
