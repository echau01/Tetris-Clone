package ui;

import exceptions.CorruptedFileException;
import model.Game;
import model.Scoreboard;
import model.ScoreboardEntry;
import persistence.ScoreboardEntryFileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

// Represents the window that appears when the game ends.
// According to the linked StackOverflow post below, having multiple JFrames in a program is considered bad practice.
// A JDialog is one of the recommended options if we want to open a new window in the program. That is why
// this class extends JDialog.
// https://stackoverflow.com/a/9554657/3335320
public class GameOverDialog extends JDialog {
    private Game game;
    private JPanel gameStatsPanel;
    private JPanel buttonPanel;
    private TetrisGUI owner;

    // This field true if the user has added their score to the temporary scoreboard; false otherwise.
    private boolean addedToTempScoreboard;

    private TemporaryScoreboardManager tempScoreboardManager = TemporaryScoreboardManager.getInstance();

    // EFFECTS: creates and shows a new GameOverDialog with given owner. The GameOverDialog displays information
    //          about the given game, and displays buttons that the user can press to indicate the action(s) they
    //          want to take.
    public GameOverDialog(Game game, TetrisGUI owner) {
        super(owner, true);
        super.setTitle("Game Over!");

        this.game = game;
        this.owner = owner;
        this.addedToTempScoreboard = false;

        // Sources of inspiration for making the window:
        // The Dialog Demo project at https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        // https://docs.oracle.com/javase/tutorial/uiswing/examples/components/DialogDemoProject/src/components/DialogDemo.java

        setLayout(new GridLayout(0, 1));
        initComponents();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up the components of this GameOverDialog, and adds the components to the window.
    private void initComponents() {
        gameStatsPanel = new JPanel(new GridLayout(0, 1));
        gameStatsPanel.add(new JLabel("Game over!"));
        gameStatsPanel.add(new JLabel("Score: " + game.getScore()));
        gameStatsPanel.add(new JLabel("Lines cleared: " + game.getLinesCleared()));
        gameStatsPanel.add(new JLabel("Choose an option below by clicking the appropriate button:"));

        buttonPanel = new JPanel(new GridLayout(0, 1));
        addButtonsToButtonPanel();

        add(gameStatsPanel);
        add(buttonPanel);

        // The following three lines of code come from https://stackoverflow.com/a/4472624/3335320.
        // Without this code, pressing the space bar will cause the buttons to be "pressed".

        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to buttonPanel.
    private void addButtonsToButtonPanel() {
        // https://docs.oracle.com/javase/tutorial/uiswing/components/button.html taught me how to
        // make buttons. The code in the implementations of the methods below is adapted from the code
        // and explanations on that website.
        addReplayButton();
        addAddTempScoreButton();
        addRemoveTempScoresButton();
        addSaveTempScoresButton();
        addViewTempScoresButton();
        addViewSavedScoresButton();
        addQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user start a new game
    private void addReplayButton() {
        JButton replayButton = new JButton("Start a new game");
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // https://stackoverflow.com/a/1235283/3335320 taught me how to close the GameOverDialog
                // https://stackoverflow.com/a/2731729/3335320 taught me how to access "this" instance
                // of GameOverDialog from inside the anonymous class
                owner.startNewGame();
                GameOverDialog.this.dispose();
            }
        });
        buttonPanel.add(replayButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user add their most recent score to
    //          the temporary scoreboard, if they have not already added the score.
    private void addAddTempScoreButton() {
        JButton addTempScoreButton = new JButton("Add your score to the temporary scoreboard");
        addTempScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!addedToTempScoreboard) {
                    String name = JOptionPane.showInputDialog("Please enter your name:");
                    // According to https://stackoverflow.com/a/42879062/3335320, name is null if
                    // the user presses "Cancel". I found out by myself that name is also null if the user
                    // closes the popup window.
                    if (name != null) {
                        ScoreboardEntry entry = new ScoreboardEntry(game.getScore(), name, game.getLinesCleared());
                        tempScoreboardManager.addTempScoreboardEntry(entry);
                        JOptionPane.showMessageDialog(null,
                                "Successfully added score to temporary scoreboard.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        addedToTempScoreboard = true;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You have already added your score.");
                }
            }
        });
        buttonPanel.add(addTempScoreButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user remove scores on the temporary scoreboard.
    private void addRemoveTempScoresButton() {
        JButton removeTempScoresButton = new JButton("Remove scores from the temporary scoreboard");
        buttonPanel.add(removeTempScoresButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user save the entries on the temporary scoreboard
    private void addSaveTempScoresButton() {
        JButton saveTempScoresButton = new JButton("Permanently save temporary scoreboard to file");
        saveTempScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempScoreboardManager.getTempScoreboardSize() == 0) {
                    JOptionPane.showMessageDialog(null, "You have no unsaved scoreboard entries.");
                    return;
                }
                try {
                    tempScoreboardManager.saveTempScoreboard();
                    JOptionPane.showMessageDialog(null,
                            "Successfully saved scoreboard entries to " + TemporaryScoreboardManager.ENTRIES_FILE_PATH,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Could not save scoreboard entries to "
                            + TemporaryScoreboardManager.ENTRIES_FILE_PATH, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(saveTempScoresButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user view the entries on the temporary scoreboard
    //          sorted from greatest to least.
    private void addViewTempScoresButton() {
        JButton viewTempScoresButton = new JButton("View unsaved scoreboard entries");
        viewTempScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ScoreboardEntry> sortedEntries = tempScoreboardManager.getSortedTempScoreboardEntries();
                if (sortedEntries.size() == 0) {
                    JOptionPane.showMessageDialog(null, "You have no unsaved scoreboard entries.");
                } else {
                    displayScoreboardEntries(sortedEntries, "Unsaved Scoreboard Entries");
                }
            }
        });
        buttonPanel.add(viewTempScoresButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that lets the user view permanently-saved scoreboard entries
    //          sorted from greatest to least
    private void addViewSavedScoresButton() {
        JButton saveTempScoresButton = new JButton("View permanently-saved scoreboard entries stored in file");
        saveTempScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(TemporaryScoreboardManager.ENTRIES_FILE_PATH);
                try {
                    file.createNewFile();
                    Scoreboard scoreboardFromFile = ScoreboardEntryFileReader.readInScoreboardEntries(file);
                    if (scoreboardFromFile.getSize() == 0) {
                        JOptionPane.showMessageDialog(null, "You have no permanently-saved scores.");
                    } else {
                        displayScoreboardEntries(scoreboardFromFile.getSortedEntries(),"Permanently-Saved Scoreboard");
                    }
                } catch (CorruptedFileException ex) {
                    JOptionPane.showMessageDialog(null, TemporaryScoreboardManager.ENTRIES_FILE_PATH
                            + " is corrupted.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Could not retrieve saved scores from "
                            + TemporaryScoreboardManager.ENTRIES_FILE_PATH, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(saveTempScoresButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to buttonPanel that allows the user to quit the program
    private void addQuitButton() {
        JButton quitButton = new JButton("Quit the program");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Credit to https://stackoverflow.com/a/1235994/3335320 for teaching me how to
                // send a window-closing event to owner.
                // The event triggers owner's window listener, which prompts the user to save
                // their unsaved scoreboard entries (if there are any).
                owner.dispatchEvent(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
                owner.dispose();
                GameOverDialog.this.dispose();
                System.exit(0);
            }
        });
        buttonPanel.add(quitButton);
    }

    // EFFECTS: creates and shows a new window that displays the scoreboard entries in the given list.
    //          The window will have the given title. The scoreboard entries are shown in the order that
    //          they appear in the list; they are *not* sorted first.
    private void displayScoreboardEntries(List<ScoreboardEntry> entries, String title) {
        JDialog dialog = new JDialog(owner, true);

        JPanel panel = new JPanel(new GridLayout(0, 4, 10, 0));
        panel.add(new JLabel("Rank"));
        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Score"));
        panel.add(new JLabel("Lines cleared"));

        for (int i = 0; i < entries.size(); i++) {
            ScoreboardEntry entry = entries.get(i);
            panel.add(new JLabel(String.valueOf(i + 1)));
            panel.add(new JLabel(entry.getPlayerName()));
            panel.add(new JLabel(String.valueOf(entry.getScore())));
            panel.add(new JLabel(String.valueOf(entry.getLinesCleared())));
        }

        // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html#scrollbars taught me
        // how to make a scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);

        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.add(scrollPane);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
