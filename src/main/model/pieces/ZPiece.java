package model.pieces;

import model.Game;

// Represents a "Z" piece
public class ZPiece extends Tetromino {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates an "Z" piece in given game. The piece is placed at the topmost row.
    public ZPiece(Game game) {
        super.game = game;
    }
}
