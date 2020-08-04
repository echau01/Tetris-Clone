package ui;

import model.Game;
import model.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

// This panel displays the next piece that will spawn in the Tetris game.
public class NextPiecePanel extends JPanel {
    private Piece nextPiece;

    // EFFECTS: constructs a NextPiecePanel to display the given next piece
    public NextPiecePanel(Piece nextPiece) {
        this.nextPiece = nextPiece;
    }

    // EFFECTS: sets the next piece to the given nextPiece
    public void setNextPiece(Piece nextPiece) {
        this.nextPiece = nextPiece;
    }

    // EFFECTS: draws the next piece
    @Override
    public void paintComponent(Graphics g) {
        Set<Point> tileLocations = nextPiece.getTileLocations();

        int lowestXPosition = Game.WIDTH;
        for (Point point : tileLocations) {
            if (point.x < lowestXPosition) {
                lowestXPosition = point.x;
            }
        }

        for (Point point : tileLocations) {
            g.setColor(Color.RED);
            g.fillRect((point.x - lowestXPosition) * BoardPanel.TILE_SIDE_LENGTH,
                    point.y * BoardPanel.TILE_SIDE_LENGTH,
                    BoardPanel.TILE_SIDE_LENGTH,
                    BoardPanel.TILE_SIDE_LENGTH);
            g.setColor(Color.BLACK);
            g.drawRect((point.x - lowestXPosition) * BoardPanel.TILE_SIDE_LENGTH,
                    point.y * BoardPanel.TILE_SIDE_LENGTH,
                    BoardPanel.TILE_SIDE_LENGTH,
                    BoardPanel.TILE_SIDE_LENGTH);
        }
    }
}
