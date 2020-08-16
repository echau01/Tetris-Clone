package ui.util;

import model.Scoreboard;
import model.ScoreboardEntry;
import ui.Main;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

// This class stores the temporary scoreboard (a scoreboard that the user can add their
// scores to before permanently saving them to file).
public class TemporaryScoreboardManager {
    public static final String ENTRIES_FILE_PATH = Main.TETRIS_DIRECTORY + "\\data\\scoreboardEntries.txt";

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

    // EFFECTS: returns the temporary scoreboard
    public Scoreboard getTempScoreboard() {
        return tempScoreboard;
    }

    // MODIFIES: this
    // EFFECTS: appends all the entries in the temporary scoreboard to file with path ENTRIES_FILE_PATH,
    //          then clears the temporary scoreboard. The file is created if it does not already exist. If
    //          the scoreboard was saved successfully, a dialog window is displayed telling the user that the
    //          save operation was successful.
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
        JOptionPane.showMessageDialog(null, "Successfully saved scoreboard entries to "
                        + ENTRIES_FILE_PATH, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
