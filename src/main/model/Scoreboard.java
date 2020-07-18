package model;

import java.util.NavigableSet;

// Represents a sorted collection of scoreboard entries.
public class Scoreboard {

    // MODIFIES: this
    // EFFECTS: adds an entry to the scoreboard
    public void addEntry(ScoreboardEntry entry) {
        //stub
    }

    // EFFECTS: returns a set of the entries on this scoreboard, sorted from the "greatest" entry
    //          to the "least" entry (as given by the compareTo method in the ScoreboardEntry class).
    public NavigableSet<ScoreboardEntry> getEntries() {
        return null;    //stub
    }
}
