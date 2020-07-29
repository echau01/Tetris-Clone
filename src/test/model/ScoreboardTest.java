package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the Scoreboard class
public class ScoreboardTest {
    private Scoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, scoreboard.getSize());
    }

    @Test
    public void testAdd() {
        ScoreboardEntry entry1 = new ScoreboardEntry(5000, "test", 36);
        ScoreboardEntry entry2 = new ScoreboardEntry(4000, "Bobby", 36);
        ScoreboardEntry entry3 = new ScoreboardEntry(6000, "Amanda", 36);
        ScoreboardEntry entry4 = new ScoreboardEntry(5000, "Thomas", 30);
        ScoreboardEntry entry5 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry6 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry7 = new ScoreboardEntry(5000, "Z", 36);

        scoreboard.add(entry1);
        scoreboard.add(entry2);
        scoreboard.add(entry3);
        scoreboard.add(entry4);
        scoreboard.add(entry5);
        scoreboard.add(entry6);
        scoreboard.add(entry7);

        assertEquals(7, scoreboard.getSize());
    }

    @Test
    public void testGetSortedEntries() {
        ScoreboardEntry entry1 = new ScoreboardEntry(5000, "test", 36);
        ScoreboardEntry entry2 = new ScoreboardEntry(4000, "Bobby", 36);
        ScoreboardEntry entry3 = new ScoreboardEntry(6000, "Amanda", 36);
        ScoreboardEntry entry4 = new ScoreboardEntry(5000, "Thomas", 30);
        ScoreboardEntry entry5 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry6 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry7 = new ScoreboardEntry(5000, "Z", 36);

        scoreboard.add(entry1);
        scoreboard.add(entry2);
        scoreboard.add(entry3);
        scoreboard.add(entry4);
        scoreboard.add(entry5);
        scoreboard.add(entry6);
        scoreboard.add(entry7);

        List<ScoreboardEntry> sortedEntries = scoreboard.getSortedEntries();
        assertEquals(7, sortedEntries.size());

        assertEquals(entry3, sortedEntries.get(0));

        // entry5 and entry6 are interchangeable here, since entry5.equals(entry6).
        assertEquals(entry5, sortedEntries.get(1));
        assertEquals(entry6, sortedEntries.get(2));

        // Note: "Z" comes before "test" because "Z" is uppercase and "t" is lowercase.
        assertEquals(entry7, sortedEntries.get(3));
        assertEquals(entry1, sortedEntries.get(4));

        assertEquals(entry4, sortedEntries.get(5));
        assertEquals(entry2, sortedEntries.get(6));
    }

    @Test
    public void testGetEntries() {
        ScoreboardEntry entry1 = new ScoreboardEntry(5000, "test", 36);
        ScoreboardEntry entry2 = new ScoreboardEntry(4000, "Bobby", 36);
        ScoreboardEntry entry3 = new ScoreboardEntry(6000, "Amanda", 36);
        ScoreboardEntry entry4 = new ScoreboardEntry(5000, "Thomas", 30);
        ScoreboardEntry entry5 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry6 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry7 = new ScoreboardEntry(5000, "Z", 36);

        scoreboard.add(entry1);
        scoreboard.add(entry2);
        scoreboard.add(entry3);
        scoreboard.add(entry4);
        scoreboard.add(entry5);
        scoreboard.add(entry6);
        scoreboard.add(entry7);

        List<ScoreboardEntry> entries = scoreboard.getEntries();
        assertEquals(7, entries.size());
        assertEquals(1, numOccurrences(entries, entry1));
        assertEquals(1, numOccurrences(entries, entry2));
        assertEquals(1, numOccurrences(entries, entry3));
        assertEquals(1, numOccurrences(entries, entry4));
        assertEquals(2, numOccurrences(entries, entry5));
        assertEquals(2, numOccurrences(entries, entry6));
        assertEquals(1, numOccurrences(entries, entry7));
    }

    // EFFECTS: returns the number of times given entry is found in entries list.
    private int numOccurrences(List<ScoreboardEntry> entries, ScoreboardEntry entry) {
        int result = 0;
        for (ScoreboardEntry se : entries) {
            if (entry.equals(se)) {
                result++;
            }
        }
        return result;
    }
}
