package model;

import persistence.Saveable;

import java.io.PrintWriter;
import java.util.Objects;

// Represents an entry on the scoreboard that is shown at the end of the game.
// An entry contains a player's score, name, and lines cleared. The level
// that the player reached is not included in the entry -- score and lines cleared
// are much better measures of skill.
public class ScoreboardEntry implements Comparable<ScoreboardEntry>, Saveable {
    private int score;
    private String playerName;
    private int linesCleared;

    // EFFECTS: creates a new scoreboard entry with the given score, player's name,
    //          and number of lines cleared
    public ScoreboardEntry(int score, String playerName, int linesCleared) {
        this.score = score;
        this.playerName = playerName;
        this.linesCleared = linesCleared;
    }

    // EFFECTS: Compares this entry and otherEntry, and returns a negative integer, 0, or a positive integer,
    //          respectively, if this entry is considered less than, equal to, or greater than otherEntry.
    //          The entry with the lower score is considered the lesser entry.
    //          If both scores are equal, then the entry with the lower number of lines cleared is
    //          considered the lesser entry.
    //          If both scores and number of lines cleared are equal, then the entry with the player name
    //          that is lexicographically "less" than the other player name, according to the String.compareTo
    //          method, is considered the *greater* (not lesser) entry.
    //          If the scores, number of lines cleared, and player names are equal, then the entries are equal.
    @Override
    public int compareTo(ScoreboardEntry otherEntry) {
        if (this.score > otherEntry.score) {
            return 1;
        } else if (this.score < otherEntry.score) {
            return -1;
        } else {
            if (this.linesCleared > otherEntry.linesCleared) {
                return 1;
            } else if (this.linesCleared < otherEntry.linesCleared) {
                return -1;
            } else {
                return -1 * this.playerName.compareTo(otherEntry.playerName);
            }
        }
    }

    // EFFECTS: returns true if obj's actual type is ScoreboardEntry and the score, player name, and
    //          lines cleared of this object and obj are equal.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ScoreboardEntry otherEntry = (ScoreboardEntry) obj;
        return score == otherEntry.score
                && linesCleared == otherEntry.linesCleared
                && playerName.equals(otherEntry.playerName);
    }

    // EFFECTS: returns the hash code for this scoreboard entry.
    @Override
    public int hashCode() {
        return Objects.hash(score, playerName, linesCleared);
    }

    // EFFECTS: returns the player's score
    public int getScore() {
        return score;
    }

    // EFFECTS: returns the player's name
    public String getPlayerName() {
        return playerName;
    }

    // EFFECTS: returns the number of lines cleared by the player
    public int getLinesCleared() {
        return linesCleared;
    }

    // MODIFIES: printWriter
    // EFFECTS: writes the fields of this scoreboard entry to the given printWriter.
    // NOTE: does not close the printWriter.
    @Override
    public void saveTo(PrintWriter printWriter) {
        // https://stackoverflow.com/a/2885224/3335320 taught me how to write to a PrintWriter
        printWriter.println(score);
        printWriter.println(playerName);
        printWriter.println(linesCleared);
    }

    // EFFECTS: returns a String of the form "Name: <player name>; Score: <score>; Lines cleared: <lines cleared>"
    @Override
    public String toString() {
        // I got the idea to override toString() from the toString() method of the Account class of the
        // TellerApp repository.
        // https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/model/Account.java
        return "Name: " + playerName + "; Score: " + score + "; Lines cleared: " + linesCleared;
    }
}
