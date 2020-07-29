package persistence;

import exceptions.CorruptedFileException;
import model.Scoreboard;
import model.ScoreboardEntry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardEntryFileReaderTest {

    @Test
    public void testReadInScoreboardEntriesNonCorrupted() {
        File file = new File("./data/testScoreboardEntries.txt");

        try {
            Scoreboard scoreboard = ScoreboardEntryFileReader.readInScoreboardEntries(file);
            ScoreboardEntry entry1 = new ScoreboardEntry(1000, "John Smith", 12);
            ScoreboardEntry entry2 = new ScoreboardEntry(25400, "Jane Doe", 40);

            List<ScoreboardEntry> entries = scoreboard.getSortedEntries();

            assertTrue(entry2.equals(entries.get(0)));
            assertTrue(entry1.equals(entries.get(1)));
            assertEquals(2, entries.size());
        } catch (CorruptedFileException e) {
            fail("CorruptedFileException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadInScoreboardEntriesCorrupted1() {
        File file = new File("./data/corruptedScoreboardEntries1.txt");

        try {
            ScoreboardEntryFileReader.readInScoreboardEntries(file);
            fail("CorruptedFileException should be thrown");
        } catch (CorruptedFileException e) {
            assertEquals("Line 6 could not be parsed into an integer.", e.getMessage());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadInScoreboardEntriesCorrupted2() {
        File file = new File("./data/corruptedScoreboardEntries2.txt");

        try {
            ScoreboardEntryFileReader.readInScoreboardEntries(file);
            fail("CorruptedFileException should be thrown");
        } catch (CorruptedFileException e) {
            assertEquals("File is badly formatted.", e.getMessage());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadInScoreboardEntriesCorrupted3() {
        File file = new File("./data/corruptedScoreboardEntries3.txt");

        try {
            ScoreboardEntryFileReader.readInScoreboardEntries(file);
            fail("CorruptedFileException should be thrown");
        } catch (CorruptedFileException e) {
            assertEquals("Line 4 could not be parsed into an integer.", e.getMessage());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadInScoreboardEntriesFileDoesNotExist() {
        File file = new File("./random/file/path");

        try {
            ScoreboardEntryFileReader.readInScoreboardEntries(file);
            fail("IOException should be thrown");
        } catch (CorruptedFileException e) {
            fail("CorruptedFileException should not be thrown");
        } catch (IOException e) {
            // expected
        }
    }
}
