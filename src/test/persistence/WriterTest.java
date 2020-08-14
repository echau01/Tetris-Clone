package persistence;

import exceptions.CorruptedFileException;
import model.Scoreboard;
import model.ScoreboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriterTest {
    private Writer testWriter;
    private ScoreboardEntry entry;
    private static final String TEST_SCOREBOARD_FILE = "./data/test/testScoreboardEntries.txt";
    private Scoreboard scoreboardInTestFile;

    @BeforeEach
    public void setUp() {
        try {
            scoreboardInTestFile = ScoreboardEntryFileReader.readInScoreboardEntries(new File(TEST_SCOREBOARD_FILE));
            testWriter = new Writer(new PrintWriter(new File(TEST_SCOREBOARD_FILE), "UTF-8"));
            entry = new ScoreboardEntry(10000, "My Name", 64);
        } catch (CorruptedFileException e) {
            fail("CorruptedFileException should not be thrown.");
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testWrite() {
        assertEquals(2, scoreboardInTestFile.getSize());

        for (ScoreboardEntry entryInFile : scoreboardInTestFile.getEntries()) {
            testWriter.write(entryInFile);
        }
        testWriter.write(entry);

        // This line must be here; otherwise, the file's contents are deleted.
        testWriter.close();

        try {
            // https://stackoverflow.com/a/4716521/3335320 and https://stackoverflow.com/a/16919543/3335320 taught
            // me how to read a file's contents and turn a file path string to a Path object
            List<String> lines = Files.readAllLines(Paths.get(TEST_SCOREBOARD_FILE));
            assertEquals(9, lines.size());
            ScoreboardEntry entry1 = new ScoreboardEntry(Integer.parseInt(lines.get(0)), lines.get(1),
                    Integer.parseInt(lines.get(2)));
            ScoreboardEntry entry2 = new ScoreboardEntry(Integer.parseInt(lines.get(3)), lines.get(4),
                    Integer.parseInt(lines.get(5)));
            ScoreboardEntry entry3 = new ScoreboardEntry(Integer.parseInt(lines.get(6)), lines.get(7),
                    Integer.parseInt(lines.get(8)));

            assertEquals(entry1, new ScoreboardEntry(1000, "John Smith", 12));
            assertEquals(entry2, new ScoreboardEntry(25400, "Jane Doe", 40));
            assertEquals(entry3, entry);

            // Reset the test file to how it was before
            // https://stackoverflow.com/a/2885224/3335320 taught me how to write to a file
            PrintWriter printWriter = new PrintWriter(new File(TEST_SCOREBOARD_FILE), "UTF-8");
            for (int i = 0; i < 6; i++) {
                printWriter.println(lines.get(i));
            }
            printWriter.close();
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
}
