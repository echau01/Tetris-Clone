package model;

// Represents an entry on the scoreboard that is shown at the end of the game.
// An entry contains the player's score, name, and level reached
public class ScoreboardEntry implements Comparable<ScoreboardEntry> {

    // EFFECTS: Compares this entry and otherEntry, and returns -1, 0, or 1, respectively, if this entry
    //          is considered less than, equal to, or greater than otherEntry.
    //          We compare scores first, then levels reached if the scores are equal, then the player names
    //          if both scores and levels reached are equal.
    @Override
    public int compareTo(ScoreboardEntry otherEntry) {
        return 0;   //stub
    }

    // EFFECTS: returns the player's score
    public int getScore() {
        return 0;   //stub
    }

    // EFFECTS: returns the player's name
    public String getPlayerName() {
        return "";  //stub
    }

    // EFFECTS: returns the level reached by the player
    public int getLevelReached() {
        return 0;   //stub
    }
}
