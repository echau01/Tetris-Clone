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
public class TetrisGui extends JFrame implements Observer {
    /* Code adapted from SimpleDrawingPlayer-Complete's DrawingEditor class.
     * https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Complete/blob/master/src/ui/DrawingEditor.java
     */

    private Game game;
    private BoardPanel boardPanel;
    private GameInfoPanel gameInfoPanel;
    private TemporaryScoreboardManager tempScoreboardManager = TemporaryScoreboardManager.getInstance();
    private ThemeSongPlayer player = ThemeSongPlayer.getInstance();

    // EFFECTS: Creates a new TetrisGui object. The GUI will have the title "Tetris".
    //          Makes a dialog window that tells the player to set the game's starting level. The GUI will only
    //          be fully initialized after the user has set the starting level in the dialog window.
    //
    //          When fully initialized, the GUI will be visible. It will have a key listener for handling keyboard
    //          controls. If the user closes the window while having at least one unsaved scoreboard entry, the user
    //          will be prompted to save any unsaved scoreboard entries they have.
    public TetrisGui() {
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
    // EFFECTS: creates and shows a dialog window that tells the player to set the game's starting level.
    //          After the player has set the starting level, a new Tetris game with a random seed will start.
    public void startNewGame() {
        new PreGameDialog(this);
    }

    // MODIFIES: this
    // EFFECTS: starts a new Tetris game with the given starting level and a random seed.
    //          No dialog window appears if this method is called.
    public void startNewGame(int startingLevel) {
        // https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java taught me
        // how to remove all components from the window
        this.getContentPane().removeAll();
        initFields(startingLevel);
        initGraphics();

        try {
            player.startThemeOnLoop();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: theme song cannot be played!",
                    "Music Error", JOptionPane.ERROR_MESSAGE);
        }

        this.repaint();
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
                // Repaints all components of the JFrame, according to https://stackoverflow.com/a/11708728/3335320
                repaint();
                player.stop();
                new GameOverDialog(observedGame, this);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: tells the window what to do when the user tries to close the window with the "X" button.
    //          If the user has any unsaved scoreboard entries and tries to close the window, the user is prompted to
    //          save their unsaved entries.
    private void setUpClosingBehaviour() {
        // The following code, which prompts the user to save their temporary scoreboard before exiting
        // the program, is adapted from this StackOverflow post: https://stackoverflow.com/a/34039602/3335320
        // In addition, https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html taught me
        // how to make dialog popup windows.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (tempScoreboardManager.getTempScoreboard().getSize() != 0) {
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
                TetrisGui.this.dispose();
                System.exit(0);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes all the fields of this GUI to their default values. The game
    //          is initialized to have the specified starting level.
    private void initFields(int gameStartingLevel) {
        game = new Game(new Random().nextInt(), gameStartingLevel);
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
