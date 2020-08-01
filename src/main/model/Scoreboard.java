package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a collection of scoreboard entries.
public class Scoreboard {
    // Note: the list is not guaranteed to be sorted
    private List<ScoreboardEntry> entries;

    // EFFECTS: creates an empty scoreboard
    public Scoreboard() {
        entries = new ArrayList<ScoreboardEntry>();
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the scoreboard
    public void add(ScoreboardEntry entry) {
        entries.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: sorts the entries on this scoreboard from the "greatest" entry to the "least" entry
    //          (where entries are compared with the compareTo method in the ScoreboardEntry class),
    //          then returns the sorted entries.
    public List<ScoreboardEntry> getSortedEntries() {
        // I discovered Collections.reverseOrder() from the JavaDocs:
        // https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#reverseOrder-java.util.Comparator-
        entries.sort(Collections.reverseOrder());
        return entries;
    }

    // EFFECTS: returns a list containing all the entries on this scoreboard. The entries
    //          do not come in any particular order.
    public List<ScoreboardEntry> getEntries() {
        return entries;
    }

    // EFFECTS: returns the number of entries on the scoreboard
    public int getSize() {
        return entries.size();
    }
}
