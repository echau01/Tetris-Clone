package ui;

import model.Scoreboard;
import model.ScoreboardEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Represents a dialog that displays a scoreboard.
public class ScoreboardDialog extends JDialog {
    private static final int MAXIMUM_WIDTH = 700;
    private static final int MAXIMUM_HEIGHT = 500;

    protected Scoreboard scoreboard;

    // EFFECTS: creates a ScoreboardDialog that displays the given scoreboard. The dialog has the given title.
    //          Note: to display the dialog, call the display() method after invoking the constructor.
    public ScoreboardDialog(Scoreboard scoreboard, String title) {
        setModal(true);
        setTitle(title);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.scoreboard = scoreboard;
    }

    // MODIFIES: this
    // EFFECTS: displays this ScoreboardDialog. The entries on the scoreboard are sorted from greatest to least.
    public void display() {
        JPanel scoreboardPanel = makeScoreboardPanel(scoreboard);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html#scrollbars taught me
        // how to make a scroll pane
        JScrollPane scrollPane = new JScrollPane(scoreboardPanel);

        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: scoreboard
    // EFFECTS: sorts the entries on the scoreboard from greatest to least, then returns a JPanel that displays
    //          the sorted entries.
    protected JPanel makeScoreboardPanel(Scoreboard scoreboard) {
        List<ScoreboardEntry> sortedEntries = scoreboard.getSortedEntries();

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

    // EFFECTS: returns the preferred size of this dialog. If the preferred size has been set to a non-null value,
    //          then returns the JDialog's preferred size with no modification. Otherwise, returns
    //          the JDialog's preferred size constrained such that its width is <= MAXIMUM_WIDTH, and
    //          its height is <= MAXIMUM_HEIGHT.
    @Override
    public Dimension getPreferredSize() {
        // This code is adapted from from https://stackoverflow.com/a/6310089/3335320
        // and https://stackoverflow.com/a/21061550/3335320.

        Dimension preferredSize = super.getPreferredSize();
        if (isPreferredSizeSet()) {
            return preferredSize;
        }

        int preferredWidth = (int) preferredSize.getWidth();
        int preferredHeight = (int) preferredSize.getHeight();

        if (preferredWidth > MAXIMUM_WIDTH) {
            preferredWidth = MAXIMUM_WIDTH;
        }
        if (preferredHeight > MAXIMUM_HEIGHT) {
            preferredHeight = MAXIMUM_HEIGHT;
        }

        return new Dimension(preferredWidth, preferredHeight);
    }
}
