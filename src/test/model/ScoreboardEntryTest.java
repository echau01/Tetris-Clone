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
    public void testToString() {
        assertEquals("Name: My Name; Score: 10000; Lines cleared: 64", entry.toString());
    }
}
