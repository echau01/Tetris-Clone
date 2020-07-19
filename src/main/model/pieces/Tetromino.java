package model.pieces;

import model.Game;

import java.awt.Point;
import java.util.*;

// Represents a tetromino (a.k.a. piece). In Tetris, there are seven types of
// tetrominoes: the I, T, O, S, Z, J, and L tetrominoes. When a tetromino is instantiated,
// its default orientation matches the orientations in the left column of this image:
// https://strategywiki.org/wiki/File:Tetris_rotation_Sega.png (except for the O piece, which
// is invariant under rotations).
public abstract class Tetromino {
    protected Game game;
    protected Map<Integer, Set<Point>> orientationToTileRelativeLocation;

    // This is an integer from 0 to 3
    protected int orientation;

    protected BoundingBox boundingBox;

    // Every tetromino has a bounding box, which is a square encompassing the tetromino.
    // The bounding box is used to store information about the tetromino's position
    // and to help the tetromino rotate.
    protected static class BoundingBox {
        int sideLength;
        Point upperLeftCorner;

        BoundingBox(int sideLength, Point upperLeftCorner) {
            this.sideLength = sideLength;
            this.upperLeftCorner = upperLeftCorner;
        }
    }

    // MODIFIES: this
    // EFFECTS: rotates the tetromino 90 degrees clockwise. If successful, returns true.
    //          If the rotation results in this tetromino intersecting a wall or an occupied cell,
    //          returns false and does not perform the rotation.
    public boolean rotate() {
        Set<Point> previousTileLocations = getTileLocations();
        nextOrientation();

        if (cannotExecuteMove(previousTileLocations)) {
            previousOrientation();
            return false;
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one column left if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveLeft() {
        Set<Point> previousTileLocations = getTileLocations();
        boundingBox.upperLeftCorner.x -= 1;

        if (cannotExecuteMove(previousTileLocations)) {
            boundingBox.upperLeftCorner.x += 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one column right if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveRight() {
        Set<Point> previousTileLocations = getTileLocations();
        boundingBox.upperLeftCorner.x += 1;

        if (cannotExecuteMove(previousTileLocations)) {
            boundingBox.upperLeftCorner.x -= 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one row down if there is space. Returns true
    //          if the move is successful; otherwise, does nothing and returns false.
    public boolean moveDown() {
        Set<Point> previousTileLocations = getTileLocations();
        boundingBox.upperLeftCorner.y += 1;

        if (cannotExecuteMove(previousTileLocations)) {
            boundingBox.upperLeftCorner.y -= 1;
            return false;
        }

        nextBoard(previousTileLocations);
        return true;
    }

    // EFFECTS: returns a set containing the locations (as points) of each of the tetromino's tiles.
    //          Changing this set does not change the location of the tiles.
    public Set<Point> getTileLocations() {
        Set<Point> tileAbsoluteLocations = new HashSet<>();
        for (Point p : orientationToTileRelativeLocation.get(orientation)) {
            // For some reason, if we try to use p.x and p.y directly in the Point constructor,
            // things go crazy.
            int posX = p.x;
            int posY = p.y;
            tileAbsoluteLocations.add(new Point(posX + boundingBox.upperLeftCorner.x,
                    posY += boundingBox.upperLeftCorner.y));
        }
        return tileAbsoluteLocations;
    }

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
    //          cannot move to this tetromino's tile locations. Returns false otherwise.
    private boolean cannotExecuteMove(Set<Point> previousTileLocations) {
        boolean obstructed = false;
        for (Point p : previousTileLocations) {
            if (!this.getTileLocations().contains(p)) {
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

    // MODIFIES: game
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
