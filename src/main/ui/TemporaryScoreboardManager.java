package ui;

import model.Scoreboard;
import model.ScoreboardEntry;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

// This class stores the temporary scoreboard (a scoreboard that the user can add their
// scores to before permanently saving them to file).
public class TemporaryScoreboardManager {
    public static final String ENTRIES_FILE_PATH = "./data/scoreboardEntries.txt";

    // We are using the singleton design pattern for this class.
    private static TemporaryScoreboardManager instance;

    private Scoreboard tempScoreboard;

    // EFFECTS: constructs a TemporaryScoreboardManager with an empty temporary scoreboard
    private TemporaryScoreboardManager() {
        tempScoreboard = new Scoreboard();
    }

    // MODIFIES: this
    // EFFECTS: returns the singleton instance
    public static TemporaryScoreboardManager getInstance() {
        if (instance == null) {
            instance = new TemporaryScoreboardManager();
        }
        return instance;
    }

    // MODIFIES: this
    // EFFECTS: adds given entry to the temporary scoreboard
    public void addTempScoreboardEntry(ScoreboardEntry entry) {
        tempScoreboard.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: sorts the temporary scoreboard from greatest to least entry, then removes the entries
    //          at the given indices. Any index i such that i < 0 or i >= getTempScoreboardSize() is ignored.
    public void removeSortedTempScoreboardEntries(Set<Integer> indices) {
        List<ScoreboardEntry> entries = tempScoreboard.getSortedEntries();

        // The following algorithm for removing multiple elements of a List given their indices comes
        // from this StackOverflow post:
        // https://stackoverflow.com/a/29656309/3335320
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (indices.contains(i)) {
                entries.remove(i);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: returns a list of the temporary scoreboard entries sorted from greatest to least entry.
    public List<ScoreboardEntry> getSortedTempScoreboardEntries() {
        return tempScoreboard.getSortedEntries();
    }

    // EFFECTS: returns the number of entries in the temporary scoreboard
    public int getTempScoreboardSize() {
        return tempScoreboard.getSize();
    }

    // MODIFIES: this
    // EFFECTS: appends all the entries in the temporary scoreboard to file with path ENTRIES_FILE_PATH,
    //          then clears the temporary scoreboard. The file is created if it does not already exist.
    //          Throws IOException if an I/O error occurs.
    public void saveTempScoreboard() throws IOException {
        File file = new File(ENTRIES_FILE_PATH);
        file.createNewFile();
        List<String> existingLines = Files.readAllLines(file.toPath());
        PrintWriter printWriter = new PrintWriter(file);
        for (String line : existingLines) {
            printWriter.println(line);
        }

        List<ScoreboardEntry> entries = tempScoreboard.getEntries();
        for (ScoreboardEntry entry : entries) {
            entry.saveTo(printWriter);
        }
        entries.clear();
        printWriter.close();
    }
}
