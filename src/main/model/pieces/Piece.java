package model.pieces;

import model.Game;

import java.awt.Point;
import java.util.*;

// Represents a piece (a.k.a. tetromino). In Tetris, there are seven types of
// pieces: the I, T, O, S, Z, J, and L pieces. When a pieces is instantiated,
// its default orientation matches the orientations in the left column of this image:
// https://strategywiki.org/wiki/File:Tetris_rotation_Sega.png (except for the O piece, which
// is invariant under rotations).
public abstract class Piece {
    protected Game game;

    // Maps orientation to locations of tiles relative to rotationReference
    protected Map<Integer, Set<Point>> orientationToTileRelativeLocation;

    // Every piece rotates inside a box, as seen in these diagrams:
    // https://strategywiki.org/wiki/File:Tetris_rotation_Sega.png
    // rotationReferencePoint is the top left corner of that box.
    protected Point rotationReferencePoint;

    // This is an integer from 0 to 3
    protected int orientation;

    // MODIFIES: this
    // EFFECTS: rotates the piece 90 degrees clockwise. If successful, returns true.
    //          If the rotation results in this piece intersecting a wall or an occupied cell,
    //          returns false and does not perform the rotation.
    public boolean rotate() {
        Set<Point> previousTileLocations = getTileLocations();
        nextOrientation();

        if (cannotExecuteMove(previousTileLocations)) {
            previousOrientation();
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this piece one column left if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveLeft() {
        Set<Point> previousTileLocations = getTileLocations();
        rotationReferencePoint.x -= 1;

        if (cannotExecuteMove(previousTileLocations)) {
            rotationReferencePoint.x += 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this piece one column right if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveRight() {
        Set<Point> previousTileLocations = getTileLocations();
        rotationReferencePoint.x += 1;

        if (cannotExecuteMove(previousTileLocations)) {
            rotationReferencePoint.x -= 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this piece one row down if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveDown() {
        Set<Point> previousTileLocations = getTileLocations();
        rotationReferencePoint.y += 1;

        if (cannotExecuteMove(previousTileLocations)) {
            rotationReferencePoint.y -= 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // EFFECTS: returns a set containing the locations (as points) of each of the piece's tiles.
    //          Changing this set does not change the location of the tiles.
    public Set<Point> getTileLocations() {
        Set<Point> tileAbsoluteLocations = new HashSet<>();
        for (Point p : orientationToTileRelativeLocation.get(orientation)) {
            // For some reason, if we try to use p.x and p.y directly in the Point constructor,
            // things go crazy.
            int posX = p.x;
            int posY = p.y;
            tileAbsoluteLocations.add(new Point(posX + rotationReferencePoint.x,
                    posY += rotationReferencePoint.y));
        }
        return tileAbsoluteLocations;
    }

    // MODIFIES: this
    // EFFECTS: initializes Map object that maps orientation to tile location relative to rotationReferencePoint
    protected void initOrientationToRelativeLocation() {
        orientationToTileRelativeLocation = new HashMap<Integer, Set<Point>>();

        orientationToTileRelativeLocation.put(0, getOrientation0RelativeLocations());
        orientationToTileRelativeLocation.put(1, getOrientation1RelativeLocations());
        orientationToTileRelativeLocation.put(2, getOrientation2RelativeLocations());
        orientationToTileRelativeLocation.put(3, getOrientation3RelativeLocations());
    }

    // EFFECTS: returns a set of the tile locations of this piece relative to rotationReferencePoint
    //          for orientation 0
    protected abstract Set<Point> getOrientation0RelativeLocations();

    // EFFECTS: returns a set of the tile locations of this piece relative to rotationReferencePoint
    //          for orientation 1
    protected abstract Set<Point> getOrientation1RelativeLocations();

    // EFFECTS: returns a set of the tile locations of this piece relative to rotationReferencePoint
    //          for orientation 2
    protected abstract Set<Point> getOrientation2RelativeLocations();

    // EFFECTS: returns a set of the tile locations of this piece relative to rotationReferencePoint
    //          for orientation 3
    protected abstract Set<Point> getOrientation3RelativeLocations();

    // MODIFIES: this
    // EFFECTS: changes orientation to next orientation
    private void nextOrientation() {
        if (orientation == 3) {
            orientation = 0;
        } else {
            orientation++;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes orientation to previous orientation
    private void previousOrientation() {
        if (orientation == 0) {
            orientation = 3;
        } else {
            orientation--;
        }
    }

    // EFFECTS: returns true if the tiles at the given previous tile locations
    //          cannot move to this piece's tile locations. Returns false otherwise.
    private boolean cannotExecuteMove(Set<Point> previousTileLocations) {
        boolean obstructed = false;
        for (Point p : this.getTileLocations()) {
            if (!previousTileLocations.contains(p)) {
                if (p.x < 0 || p.x >= Game.WIDTH) {
                    obstructed = true;
                    break;
                } else if (p.y < 0 || p.y >= Game.HEIGHT) {
                    obstructed = true;
                    break;
                } else if (game.getBoard().get(p.y).get(p.x)) {
                    obstructed = true;
                    break;
                }
            }
        }
        return obstructed;
    }

    // MODIFIES: this
    // EFFECTS: removes the tiles at the given locations from the board, and adds current tiles to the board.
    private void nextBoard(Set<Point> tilesToRemove) {
        List<ArrayList<Boolean>> board = game.getBoard();
        for (Point p : tilesToRemove) {
            board.get(p.y).set(p.x, false);
        }
        for (Point p : getTileLocations()) {
            board.get(p.y).set(p.x, true);
        }
    }
}
