package ui;

import model.Scoreboard;
import model.ScoreboardEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a dialog that displays a scoreboard and allows the user to select entries
// that they want to remove.
public class RemoveScoresDialog extends ScoreboardDialog {
    // Maps checkboxes that the user can select to their associated scoreboard entries.
    private Map<JCheckBox, ScoreboardEntry> checkBoxToEntryMap;

    // EFFECTS: creates a RemoveScoresDialog that displays the given scoreboard. The dialog has the given title.
    //          Note: to display the dialog, call the display() method after invoking the constructor.
    public RemoveScoresDialog(Scoreboard scoreboard, String title) {
        super(scoreboard, title);

        checkBoxToEntryMap = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: displays this RemoveScoresDialog. The entries on the scoreboard are sorted from greatest to least.
    //          A checkbox is placed beside each entry. The user can use these checkboxes to select entries to remove
    //          from the scoreboard.
    @Override
    public void display() {
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html taught me how to use BoxLayout
        // https://stackoverflow.com/a/761379/3335320 introduced me to the getContentPane() method,
        // helping me avoid the "BoxLayout can't be shared" error.
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Select entries to remove below (click a checkbox to select the entry):"));

        JPanel checkBoxAndScoreboardPanel = makeCheckBoxAndScoreboardPanel();

        // The border makes the panel look nicer. https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html has
        // an example of a project that uses borders, which is where I got this line of code from:
        checkBoxAndScoreboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bottomButtonPanel = makeBottomButtonPanel();
        JScrollPane scrollPane = new JScrollPane(checkBoxAndScoreboardPanel);

        add(headerPanel);
        add(scrollPane);
        add(bottomButtonPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: returns a JPanel showing entries on the temporary scoreboard sorted from greatest to least.
    //          There is a checkbox to the left of each entry.
    private JPanel makeCheckBoxAndScoreboardPanel() {
        JPanel checkBoxAndScoreboardPanel = new JPanel();
        checkBoxAndScoreboardPanel.setLayout(new BoxLayout(checkBoxAndScoreboardPanel, BoxLayout.X_AXIS));

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(0, 1));

        JPanel scoreboardPanel = super.makeScoreboardPanel(super.scoreboard);

        JCheckBox invisibleCheckBox = new JCheckBox();
        invisibleCheckBox.setVisible(false);
        checkBoxPanel.add(invisibleCheckBox);

        List<ScoreboardEntry> entries = super.scoreboard.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            JCheckBox checkBox = new JCheckBox();
            checkBoxPanel.add(checkBox);
            checkBoxToEntryMap.put(checkBox, entries.get(i));
        }

        checkBoxAndScoreboardPanel.add(checkBoxPanel);
        checkBoxAndScoreboardPanel.add(scoreboardPanel);

        return checkBoxAndScoreboardPanel;
    }

    // MODIFIES: this
    // EFFECTS: returns a JPanel containing the two buttons at the bottom of the dialog window.
    private JPanel makeBottomButtonPanel() {
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new BoxLayout(bottomButtonPanel, BoxLayout.X_AXIS));

        JButton removeButton = makeRemoveButton();
        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveScoresDialog.this.dispose();
            }
        });

        bottomButtonPanel.add(removeButton);
        bottomButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomButtonPanel.add(cancelButton);

        return bottomButtonPanel;
    }

    // EFFECTS: sets up the "Remove selected entries" button at the bottom of the dialog window.
    private JButton makeRemoveButton() {
        JButton removeButton = new JButton("Remove selected entries");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ScoreboardEntry> entries = RemoveScoresDialog.super.scoreboard.getEntries();
                boolean removedAnEntry = false;
                for (JCheckBox checkBox : checkBoxToEntryMap.keySet()) {
                    if (checkBox.isSelected()) {
                        entries.remove(checkBoxToEntryMap.get(checkBox));
                        removedAnEntry = true;
                    }
                }

                if (!removedAnEntry) {
                    JOptionPane.showMessageDialog(null, "You did not select any entries to remove.");
                } else {
                    RemoveScoresDialog.this.dispose();
                    JOptionPane.showMessageDialog(null, "Successfully removed selected entries.");
                }
            }
        });

        return removeButton;
    }
}
