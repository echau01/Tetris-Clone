package model.pieces;

import model.Game;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// Represents a "J" piece
public class JPiece extends Tetromino {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates a "J" piece in given game. The piece is placed at the topmost row.
    public JPiece(Game game) {
        super.game = game;

        initOrientationToRelativeLocation();

        orientation = 0;
        boundingBox = new BoundingBox(3, new Point(Math.floorDiv(Game.WIDTH - 1, 2), -1));
    }

    protected void initOrientationToRelativeLocation() {
        orientationToTileRelativeLocation = new HashMap<Integer, Set<Point>>();

        setOrientation0RelativeLocations();
        setOrientation1RelativeLocations();
        setOrientation2RelativeLocations();
        setOrientation3RelativeLocations();
    }

    private void setOrientation0RelativeLocations() {
        Set<Point> orientation0RelativeLocations = new HashSet<Point>();
        orientation0RelativeLocations.add(new Point(0, 1));
        orientation0RelativeLocations.add(new Point(1, 1));
        orientation0RelativeLocations.add(new Point(2, 1));
        orientation0RelativeLocations.add(new Point(2, 2));
        orientationToTileRelativeLocation.put(0, orientation0RelativeLocations);
    }

    private void setOrientation1RelativeLocations() {
        Set<Point> orientation1RelativeLocations = new HashSet<Point>();
        orientation1RelativeLocations.add(new Point(0, 2));
        orientation1RelativeLocations.add(new Point(1, 2));
        orientation1RelativeLocations.add(new Point(1, 1));
        orientation1RelativeLocations.add(new Point(0, 1));
        orientationToTileRelativeLocation.put(1, orientation1RelativeLocations);
    }

    private void setOrientation2RelativeLocations() {
        Set<Point> orientation2RelativeLocations = new HashSet<Point>();
        orientation2RelativeLocations.add(new Point(0, 1));
        orientation2RelativeLocations.add(new Point(0, 2));
        orientation2RelativeLocations.add(new Point(1, 2));
        orientation2RelativeLocations.add(new Point(2, 2));
        orientationToTileRelativeLocation.put(2, orientation2RelativeLocations);
    }

    private void setOrientation3RelativeLocations() {
        Set<Point> orientation4RelativeLocations = new HashSet<Point>();
        orientation4RelativeLocations.add(new Point(1, 2));
        orientation4RelativeLocations.add(new Point(1, 1));
        orientation4RelativeLocations.add(new Point(1, 0));
        orientation4RelativeLocations.add(new Point(2, 0));
        orientationToTileRelativeLocation.put(3, orientation4RelativeLocations);
    }
}
