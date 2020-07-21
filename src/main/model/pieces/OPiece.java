package model.pieces;

import model.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// Represents an "O" piece
public class OPiece extends Piece {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates an "O" piece in given game. The piece is placed at the topmost row
    //          and spawns in its default orientation.
    public OPiece(Game game) {
        super(game, new Point(Math.floorDiv(Game.WIDTH - 1, 2) - 1, -1));
    }

    // EFFECTS: returns a set of the tile locations of this "O" piece relative to rotationReferencePoint
    //          for orientation 0
    @Override
    protected Set<Point> getOrientation0RelativeLocations() {
        Set<Point> orientation0RelativeLocations = new HashSet<Point>();
        orientation0RelativeLocations.add(new Point(1, 1));
        orientation0RelativeLocations.add(new Point(1, 2));
        orientation0RelativeLocations.add(new Point(2, 1));
        orientation0RelativeLocations.add(new Point(2, 2));
        return orientation0RelativeLocations;
    }

    // EFFECTS: returns a set of the tile locations of this "O" piece relative to rotationReferencePoint
    //          for orientation 1
    @Override
    protected Set<Point> getOrientation1RelativeLocations() {
        return getOrientation0RelativeLocations();
    }

    // EFFECTS: returns a set of the tile locations of this "O" piece relative to rotationReferencePoint
    //          for orientation 2
    @Override
    protected Set<Point> getOrientation2RelativeLocations() {
        return getOrientation0RelativeLocations();
    }

    // EFFECTS: returns a set of the tile locations of this "O" piece relative to rotationReferencePoint
    //          for orientation 3
    @Override
    protected Set<Point> getOrientation3RelativeLocations() {
        return getOrientation0RelativeLocations();
    }
}
