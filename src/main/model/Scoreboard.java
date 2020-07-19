package model;

import java.util.List;

// Represents a sorted collection of scoreboard entries.
public class Scoreboard {

    // EFFECTS: creates an empty scoreboard
    public Scoreboard() {

    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the scoreboard
    public void add(ScoreboardEntry entry) {
        //stub
    }

    // EFFECTS: returns a list of the entries on this scoreboard, sorted from the "greatest" entry
    //          to the "least" entry (as given by the compareTo method in the ScoreboardEntry class).
    public List<ScoreboardEntry> getEntries() {
        return null;    //stub
    }

    // EFFECTS: returns the number of entries on the scoreboard
    public int getSize() {
        return 0;   //stub
    }
}
