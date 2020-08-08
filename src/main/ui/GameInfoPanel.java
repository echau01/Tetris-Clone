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
    private Game game;

    private JLabel scoreLabel;
    private JLabel linesClearedLabel;
    private JLabel levelLabel;
    private JLabel nextPieceLabel;
    private NextPiecePanel nextPiecePanel;

    // EFFECTS: constructs a new GameInfoPanel for the given game
    public GameInfoPanel(Game game) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.game = game;
        game.addObserver(this);

        scoreLabel = new JLabel("Score: " + game.getScore());
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        linesClearedLabel = new JLabel("Lines cleared: " + game.getLinesCleared());
        linesClearedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        levelLabel = new JLabel("Level: " + game.getLevel());
        levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nextPieceLabel = new JLabel("Next piece: ");
        nextPieceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nextPiecePanel = new NextPiecePanel(game.getNextPiece());
        nextPiecePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(scoreLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(linesClearedLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(levelLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(nextPieceLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
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
