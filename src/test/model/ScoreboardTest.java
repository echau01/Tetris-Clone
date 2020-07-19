package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        ScoreboardEntry entry1 = new ScoreboardEntry(5000, "Test", 36);
        ScoreboardEntry entry2 = new ScoreboardEntry(4000, "Bobby", 36);
        ScoreboardEntry entry3 = new ScoreboardEntry(6000, "Amanda", 36);
        ScoreboardEntry entry4 = new ScoreboardEntry(5000, "Thomas", 30);
        ScoreboardEntry entry5 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry6 = new ScoreboardEntry(5000, "Felix", 36);

        scoreboard.add(entry1);
        scoreboard.add(entry2);
        scoreboard.add(entry3);
        scoreboard.add(entry4);
        scoreboard.add(entry5);
        scoreboard.add(entry6);

        assertEquals(6, scoreboard.getSize());
    }

    @Test
    public void testGetEntries() {
        ScoreboardEntry entry1 = new ScoreboardEntry(5000, "Test", 36);
        ScoreboardEntry entry2 = new ScoreboardEntry(4000, "Bobby", 36);
        ScoreboardEntry entry3 = new ScoreboardEntry(6000, "Amanda", 36);
        ScoreboardEntry entry4 = new ScoreboardEntry(5000, "Thomas", 30);
        ScoreboardEntry entry5 = new ScoreboardEntry(5000, "Felix", 36);
        ScoreboardEntry entry6 = new ScoreboardEntry(5000, "Felix", 36);

        scoreboard.add(entry1);
        scoreboard.add(entry2);
        scoreboard.add(entry3);
        scoreboard.add(entry4);
        scoreboard.add(entry5);
        scoreboard.add(entry6);

        List<ScoreboardEntry> entries = scoreboard.getEntries();
        assertEquals("Amanda", entries.get(0).getPlayerName());
        assertEquals("Felix", entries.get(1).getPlayerName());
        assertEquals(36, entries.get(1).getLinesCleared());
        assertEquals("Felix", entries.get(2).getPlayerName());
        assertEquals(36, entries.get(2).getLinesCleared());
        assertEquals("Test", entries.get(3).getPlayerName());
        assertEquals("Thomas", entries.get(4).getPlayerName());
        assertEquals("Billy", entries.get(5).getPlayerName());
    }
}
