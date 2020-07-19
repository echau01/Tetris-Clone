package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(1, entry.compareTo(entry2));
        assertEquals(-1, entry2.compareTo(entry));
    }

    @Test
    public void testCompareToSameScoreDifferentLinesCleared() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "Alexa", 65);
        assertEquals(-1, entry.compareTo(entry2));
        assertEquals(1, entry2.compareTo(entry));
    }

    @Test
    public void testCompareToSameScoreSameLinesClearedDifferentName() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "Alexa", 64);
        assertEquals(-1, entry.compareTo(entry2));
        assertEquals(1, entry2.compareTo(entry));
    }

    @Test
    public void testCompareToEqualEntries() {
        ScoreboardEntry entry2 = new ScoreboardEntry(10000, "My Name", 64);
        assertEquals(0, entry.compareTo(entry2));
        assertEquals(0, entry2.compareTo(entry));
    }
}
