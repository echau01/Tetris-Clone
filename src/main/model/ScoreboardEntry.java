package model;

// Represents an entry on the scoreboard that is shown at the end of the game.
// An entry contains the player's score, name, and lines cleared
public class ScoreboardEntry implements Comparable<ScoreboardEntry> {
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
}
