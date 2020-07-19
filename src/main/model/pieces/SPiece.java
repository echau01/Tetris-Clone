package model.pieces;

import model.Game;

// Represents an "S" piece
public class SPiece extends Tetromino {

    // REQUIRES: game has started and is not over
    // EFFECTS: creates an "S" piece in given game. The piece is placed at the topmost row.
    public SPiece(Game game) {
        super.game = game;
    }
}
