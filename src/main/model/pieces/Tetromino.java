package model.pieces;

import java.awt.Point;
import java.util.Set;

// Represents a tetromino (a.k.a. piece). In Tetris, there are seven types of
// tetrominoes: the I, T, O, S, Z, J, and L tetrominoes. When a tetromino is instantiated,
// its default orientation matches the orientations in the left column of this image:
// https://strategywiki.org/wiki/File:Tetris_rotation_Sega.png (except for the O piece, which
// is invariant under rotations).
public abstract class Tetromino {
    
    // EFFECTS: rotates the tetromino 90 degrees clockwise. If successful, returns true.
    //          If the rotation results in this tetromino intersecting a wall or an occupied cell,
    //          returns false and does not perform the rotation.
    public boolean rotate() {
        return false;   //stub
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one column left if there is space.
    //          Otherwise, do nothing.
    public void moveLeft() {
        //stub
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one column right if there is space.
    //          Otherwise, do nothing.
    public void moveRight() {
        //stub
    }

    // MODIFIES: this
    // EFFECTS: moves this tetromino one row down if there is space.
    //          Otherwise, do nothing.
    public void moveDown() {
        //stub
    }

    // EFFECTS: returns a set containing the locations (as points) of each of the tetromino's tiles
    public Set<Point> getTileLocations() {
        return null;    //stub
    }

    // Every tetromino has a bounding box, which is a square encompassing the tetromino.
    // The bounding box is used to store information about the tetromino's position
    // and to help the tetromino rotate.
    private static class BoundingBox {

    }
}
