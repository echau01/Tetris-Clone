package model;

// Represents an entry on the scoreboard that is shown at the end of the game.
// An entry contains the player's score, name, and lines cleared
public class ScoreboardEntry implements Comparable<ScoreboardEntry> {

    // EFFECTS: creates a new scoreboard entry with the given score, player's name,
    //          and number of lines cleared
    public ScoreboardEntry(int score, String playerName, int linesCleared) {
        //stub
    }

    // EFFECTS: Compares this entry and otherEntry, and returns -1, 0, or 1, respectively, if this entry
    //          is considered less than, equal to, or greater than otherEntry.
    //          We compare scores first, then number of lines cleared if the scores are equal.
    //          If both scores and number of lines cleared are equal, then the entry with the player name
    //          that is lexicographically "less" than the other player name, according to the String.compareTo
    //          method, is considered the "greater" entry.
    //          If the scores, number of lines cleared, and player names are equal, then the entries are equal.
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

    // EFFECTS: returns the number of lines cleared by the player
    public int getLinesCleared() {
        return 0;   //stub
    }
}
