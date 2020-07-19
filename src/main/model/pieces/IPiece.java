package model.pieces;

import model.Game;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// Represents an "I" piece
public class IPiece extends Tetromino {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates an "I" piece in given game. The piece is placed at the topmost row.
    public IPiece(Game game) {
        super.game = game;
        initOrientationToRelativeLocation();

        orientation = 0;
        boundingBox = new BoundingBox(4, new Point(Math.floorDiv(Game.WIDTH - 1, 2) - 1, -1));
    }

    protected void initOrientationToRelativeLocation() {
        orientationToTileRelativeLocation = new HashMap<Integer, Set<Point>>();

        Set<Point> orientation0RelativeLocations = new HashSet<Point>();
        orientation0RelativeLocations.add(new Point(0, 1));
        orientation0RelativeLocations.add(new Point(1, 1));
        orientation0RelativeLocations.add(new Point(2, 1));
        orientation0RelativeLocations.add(new Point(3, 1));
        orientationToTileRelativeLocation.put(0, orientation0RelativeLocations);

        Set<Point> orientation1RelativeLocations = new HashSet<Point>();
        orientation1RelativeLocations.add(new Point(2, 0));
        orientation1RelativeLocations.add(new Point(2, 1));
        orientation1RelativeLocations.add(new Point(2, 2));
        orientation1RelativeLocations.add(new Point(2, 3));
        orientationToTileRelativeLocation.put(1, orientation1RelativeLocations);

        orientationToTileRelativeLocation.put(2, orientation0RelativeLocations);
        orientationToTileRelativeLocation.put(3, orientation1RelativeLocations);
    }
}
