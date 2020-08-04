package ui;


import model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

// Represents the panel that tells the user information about a Tetris game. The
// panel tells the user their current score, level, and lines cleared, as well as
// what the next piece is.
public class GameInfoPanel extends JPanel implements Observer {
    public static final int WIDTH = 200;
    public static final int HEIGHT = BoardPanel.HEIGHT / 2;

    private Game game;

    private JLabel scoreLabel;
    private JLabel linesClearedLabel;
    private JLabel levelLabel;
    private JLabel nextPieceLabel;
    private NextPiecePanel nextPiecePanel;

    // EFFECTS: constructs a new GameInfoPanel for the given game
    public GameInfoPanel(Game game) {
        super(new GridLayout(0, 1));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.game = game;
        game.addObserver(this);

        scoreLabel = new JLabel("Score: " + game.getScore());
        linesClearedLabel = new JLabel("Lines cleared: " + game.getLinesCleared());
        levelLabel = new JLabel("Level: " + game.getLevel());
        nextPieceLabel = new JLabel("Next piece: ");
        nextPiecePanel = new NextPiecePanel(game.getNextPiece());

        add(scoreLabel);
        add(linesClearedLabel);
        add(levelLabel);
        add(nextPieceLabel);
        add(nextPiecePanel);
    }

    // MODIFIES: this
    // EFFECTS: if observable is an instance of Game, then updates the game information shown by this panel
    //          to reflect the changes in the Observable. The arg parameter is ignored.
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            Game observedGame = (Game) observable;
            scoreLabel.setText("Score: " + observedGame.getScore());
            linesClearedLabel.setText("Lines cleared: " + observedGame.getLinesCleared());
            levelLabel.setText("Level: " + observedGame.getLevel());
            nextPieceLabel.setText("Next piece: ");
            nextPiecePanel.setNextPiece(observedGame.getNextPiece());
        }

        repaint();
    }
}
