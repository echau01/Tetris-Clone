package ui;

import model.Scoreboard;

import javax.swing.*;
import java.awt.*;

// Represents a dialog that displays a scoreboard.
public abstract class ScoreboardDialog extends JDialog {
    // Maximum preferred width and height
    // The actual width and height could exceed these values because the user can resize the window themselves.
    private static final int MAXIMUM_WIDTH = 700;
    private static final int MAXIMUM_HEIGHT = 500;

    protected Scoreboard scoreboard;

    // EFFECTS: creates a ScoreboardDialog that displays the given scoreboard. The dialog has the given title,
    //          is resizable, and is set to be modal. Upon closing, the dialog is disposed.
    //          Note: to display the dialog, call the display() method after invoking the constructor.
    public ScoreboardDialog(Scoreboard scoreboard, String title) {
        setModal(true);
        setTitle(title);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.scoreboard = scoreboard;
    }

    // MODIFIES: this
    // EFFECTS: sorts the entries on the scoreboard from greatest to least, then displays this ScoreboardDialog,
    //          showing the entries in their sorted order.
    public abstract void display();

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
