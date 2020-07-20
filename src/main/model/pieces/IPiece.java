package model.pieces;

import model.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// Represents an "I" piece
public class IPiece extends Piece {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates an "I" piece in given game. The piece is placed at the topmost row.
    public IPiece(Game game) {
        super.game = game;
        initOrientationToRelativeLocation();

        orientation = 0;
        rotationReferencePoint = new Point(Math.floorDiv(Game.WIDTH - 1, 2) - 1, -1);
    }

    @Override
    protected Set<Point> getOrientation0RelativeLocations() {
        Set<Point> orientation0RelativeLocations = new HashSet<Point>();
        orientation0RelativeLocations.add(new Point(0, 1));
        orientation0RelativeLocations.add(new Point(1, 1));
        orientation0RelativeLocations.add(new Point(2, 1));
        orientation0RelativeLocations.add(new Point(3, 1));
        return orientation0RelativeLocations;
    }

    @Override
    protected Set<Point> getOrientation1RelativeLocations() {
        Set<Point> orientation1RelativeLocations = new HashSet<Point>();
        orientation1RelativeLocations.add(new Point(2, 0));
        orientation1RelativeLocations.add(new Point(2, 1));
        orientation1RelativeLocations.add(new Point(2, 2));
        orientation1RelativeLocations.add(new Point(2, 3));
        return orientation1RelativeLocations;
    }

    @Override
    protected Set<Point> getOrientation2RelativeLocations() {
        return getOrientation0RelativeLocations();
    }

    @Override
    protected Set<Point> getOrientation3RelativeLocations() {
        return getOrientation1RelativeLocations();
    }
}
