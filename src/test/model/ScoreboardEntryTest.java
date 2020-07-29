package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the ScoreboardEntry class
public class ScoreboardEntryTest {
    private ScoreboardEntry entry;

    @BeforeEach
    public void setUp() {
        entry = new ScoreboardEntry(10000, "My Name", 64);
    }

    @Test
    public void testConstructor() {
        assertEquals(10000, entry.getScore());
        assertEquals("My Name", entry.getPlayerName());
        assertEquals(64, entry.getLinesCleared());
    }

    @Test
    public void testCompareToDifferentScores() {
        ScoreboardEntry entry2 = new ScoreboardEntry(9999, "Alexa", 70);
        assertTrue(entry.compareTo(entry2) > 0);
        assertTrue(entry2.compareTo(entry) < 0);
    }

    @Test
    public void testCompareToSameScoreDifferentLinesCleared() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "Alexa", 65);
        assertTrue(entry.compareTo(entry2) < 0);
        assertTrue(entry2.compareTo(entry) > 0);
    }

    @Test
    public void testCompareToSameScoreSameLinesClearedDifferentName() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "Alexa", 64);
        assertTrue(entry.compareTo(entry2) < 0);
        assertTrue(entry2.compareTo(entry) > 0);
    }

    @Test
    public void testCompareToEqualEntries() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "My Name", 64);
        assertEquals(0, entry.compareTo(entry2));
        assertEquals(0, entry2.compareTo(entry));
    }

    @Test
    public void testEqualsTrue() {
        assertTrue(entry.equals(entry));

        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "My Name", 64);
        assertTrue(entry.equals(entry2));
        assertTrue(entry2.equals(entry));
    }

    @Test
    public void testEqualsFalseOneFieldDifferent() {
        ScoreboardEntry entry2 = new ScoreboardEntry(9999, "My Name", 64);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));

        entry2 = new ScoreboardEntry(10000, "My Name!", 64);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));

        entry2 = new ScoreboardEntry(10000, "My Name", 63);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));
    }

    @Test
    public void testEqualsFalseTwoFieldsDifferent() {
        ScoreboardEntry entry2 = new ScoreboardEntry(9000, "Test", 64);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));

        entry2 = new ScoreboardEntry(10000, "Test", 63);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));

        entry2 = new ScoreboardEntry(9000, "My Name", 63);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));
    }

    @Test
    public void testEqualsFalseThreeFieldsDifferent() {
        ScoreboardEntry entry2 = new ScoreboardEntry(9000, "Test", 100);
        assertFalse(entry.equals(entry2));
        assertFalse(entry2.equals(entry));
    }

    @Test
    public void testEqualsFalseDifferentTypes() {
        Object obj = new Object();
        assertFalse(entry.equals(obj));
        assertFalse(obj.equals(entry));
    }

    @Test
    public void testEqualsFalseNullObject() {
        assertFalse(entry.equals(null));
    }

    @Test
    public void testHashCode() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "My Name", 64);
        assertTrue(entry.equals(entry2));

        // Equal objects must have equal hashcodes
        assertEquals(entry.hashCode(), entry2.hashCode());
    }

    @Test
    public void testSaveTo() {
        String filePath = "./data/testScoreboardEntries.txt";
        try {
            // https://stackoverflow.com/a/4716521/3335320 and https://stackoverflow.com/a/16919543/3335320 taught
            // me how to read a file's contents and turn a file path string to a Path object
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            assertEquals(6, lines.size());

            // https://stackoverflow.com/a/2885224/3335320 taught me how to write to a file
            PrintWriter printWriter = new PrintWriter(new File(filePath), "UTF-8");
            for (String line : lines) {
                printWriter.println(line);
            }
            entry.saveTo(printWriter);

            // This line must be here; otherwise, the file's contents are deleted.
            printWriter.close();
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
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
            PrintWriter printWriter = new PrintWriter(new File(filePath), "UTF-8");
            for (int i = 0; i < 6; i++) {
                printWriter.println(lines.get(i));
            }
            printWriter.close();
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testAppendToExistingFile() {
        String filePath = "./data/testScoreboardEntries.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            assertEquals(6, lines.size());

            entry.appendTo(new File(filePath));
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
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
            PrintWriter printWriter = new PrintWriter(new File(filePath), "UTF-8");
            for (int i = 0; i < 6; i++) {
                printWriter.println(lines.get(i));
            }
            printWriter.close();
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testAppendToNonExistingFile() {
        String filePath = "./data/nonExistentFile.txt";
        File file = new File(filePath);
        try {
            entry.appendTo(file);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            assertEquals(3, lines.size());
            ScoreboardEntry savedEntry = new ScoreboardEntry(Integer.parseInt(lines.get(0)), lines.get(1),
                    Integer.parseInt(lines.get(2)));

            assertEquals(savedEntry, entry);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

        // Now delete the file to reset everything to how it was before the test ran.
        assertTrue(file.delete());
    }

    @Test
    public void testToString() {
        assertEquals("Name: My Name; Score: 10000; Lines cleared: 64", entry.toString());
    }
}
