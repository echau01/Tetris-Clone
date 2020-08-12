package ui.dialog;

import model.Scoreboard;
import model.ScoreboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Represents a dialog that only displays a scoreboard. The user cannot interact with the scoreboard in any way.
public class PlainScoreboardDisplay extends ScoreboardDialog {

    // EFFECTS: creates a PlainScoreboardDisplay that displays the given scoreboard in a dialog window. The dialog has
    //          the given title, is resizable, and is set to be modal. Upon closing, the display is disposed.
    //          Note: to display the dialog, call the display() method after invoking the constructor.
    public PlainScoreboardDisplay(Scoreboard scoreboard, String title) {
        super(scoreboard, title);
    }

    // MODIFIES: this
    // EFFECTS: sorts the entries on the scoreboard from greatest to least, then displays this PlainScoreboardDisplay,
    //          showing the entries in their sorted order.
    @Override
    public void display() {
        JPanel scoreboardPanel = makeScoreboardPanel();
        scoreboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html#scrollbars taught me
        // how to make a scroll pane
        JScrollPane scrollPane = new JScrollPane(scoreboardPanel);

        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sorts the entries on the scoreboard from greatest to least, then returns a JPanel that displays
    //          the entries in their sorted order.
    private JPanel makeScoreboardPanel() {
        List<ScoreboardEntry> sortedEntries = super.scoreboard.getSortedEntries();

        JPanel panel = new JPanel(new GridLayout(0, 4, 10, 0));
        panel.add(new JLabel("Rank"));
        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Score"));
        panel.add(new JLabel("Lines cleared"));

        for (int i = 0; i < sortedEntries.size(); i++) {
            ScoreboardEntry entry = sortedEntries.get(i);
            panel.add(new JLabel(String.valueOf(i + 1)));
            panel.add(new JLabel(entry.getPlayerName()));
            panel.add(new JLabel(String.valueOf(entry.getScore())));
            panel.add(new JLabel(String.valueOf(entry.getLinesCleared())));
        }

        return panel;
    }
}
