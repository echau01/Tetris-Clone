package test.model;

import model.ScoreboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
