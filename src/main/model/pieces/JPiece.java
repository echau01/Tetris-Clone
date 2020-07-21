package model.pieces;

import model.Game;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// Represents a "J" piece
public class JPiece extends Piece {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates a "J" piece in given game. The piece is placed at the topmost row
    //          and spawns in its default orientation.
    public JPiece(Game game) {
        super(game, new Point(Math.floorDiv(Game.WIDTH - 1, 2), -1));
    }

    // EFFECTS: returns a set of the tile locations of this "J" piece relative to rotationReferencePoint
    //          for orientation 0
    protected Set<Point> getOrientation0RelativeLocations() {
        Set<Point> orientation0RelativeLocations = new HashSet<Point>();
        orientation0RelativeLocations.add(new Point(0, 1));
        orientation0RelativeLocations.add(new Point(1, 1));
        orientation0RelativeLocations.add(new Point(2, 1));
        orientation0RelativeLocations.add(new Point(2, 2));
        return orientation0RelativeLocations;
    }

    // EFFECTS: returns a set of the tile locations of this "J" piece relative to rotationReferencePoint
    //          for orientation 1
    protected Set<Point> getOrientation1RelativeLocations() {
        Set<Point> orientation1RelativeLocations = new HashSet<Point>();
        orientation1RelativeLocations.add(new Point(0, 2));
        orientation1RelativeLocations.add(new Point(1, 2));
        orientation1RelativeLocations.add(new Point(1, 1));
        orientation1RelativeLocations.add(new Point(1, 0));
        return orientation1RelativeLocations;
    }

    // EFFECTS: returns a set of the tile locations of this "J" piece relative to rotationReferencePoint
    //          for orientation 2
    protected Set<Point> getOrientation2RelativeLocations() {
        Set<Point> orientation2RelativeLocations = new HashSet<Point>();
        orientation2RelativeLocations.add(new Point(0, 1));
        orientation2RelativeLocations.add(new Point(0, 2));
        orientation2RelativeLocations.add(new Point(1, 2));
        orientation2RelativeLocations.add(new Point(2, 2));
        return orientation2RelativeLocations;
    }

    // EFFECTS: returns a set of the tile locations of this "J" piece relative to rotationReferencePoint
    //          for orientation 3
    protected Set<Point> getOrientation3RelativeLocations() {
        Set<Point> orientation3RelativeLocations = new HashSet<Point>();
        orientation3RelativeLocations.add(new Point(1, 2));
        orientation3RelativeLocations.add(new Point(1, 1));
        orientation3RelativeLocations.add(new Point(1, 0));
        orientation3RelativeLocations.add(new Point(2, 0));
        return orientation3RelativeLocations;
    }
}
