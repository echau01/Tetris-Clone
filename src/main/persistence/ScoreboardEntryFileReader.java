package persistence;

import exceptions.CorruptedFileException;
import model.Scoreboard;
import model.ScoreboardEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// This class can read scoreboard entry information from file.
public class ScoreboardEntryFileReader {
    /* Code adapted from
     * https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank
     * /persistence/Reader.java
     */

    // EFFECTS: returns a scoreboard containing all scoreboard entries in given file.
    //          Throws CorruptedFileException if given file has been corrupted (for example,
    //          if parts of scoreboard entries are missing, or are in the wrong places).
    //          Throws IOException if an I/O error occurs.
    public static Scoreboard readInScoreboardEntries(File file) throws CorruptedFileException, IOException {
        // https://stackoverflow.com/a/15512246/3335320 taught me how to convert a File to a Path object
        List<String> lines = Files.readAllLines(file.toPath());

        if (lines.size() % 3 != 0) {
            throw new CorruptedFileException("At least one part of a scoreboard entry is missing.");
        }

        Scoreboard scoreboard = new Scoreboard();
        for (int i = 0; i < lines.size(); i += 3) {
            try {
                int score = Integer.parseInt(lines.get(i));
                String playerName = lines.get(i + 1);
                int linesCleared = Integer.parseInt(lines.get(i + 2));

                scoreboard.add(new ScoreboardEntry(score, playerName, linesCleared));
            } catch (NumberFormatException e) {
                throw new CorruptedFileException("Line " + i + " could not be parsed into an integer.");
            }
        }

        return scoreboard;
    }
}
