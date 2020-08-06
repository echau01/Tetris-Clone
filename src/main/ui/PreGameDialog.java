package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a dialog window that appears before the user starts a new game.
public class PreGameDialog extends JDialog {
    private TetrisGui gui;
    private JComboBox<Integer> levelOptions;

    // EFFECTS: creates and shows a new PreGameDialog that guides the user to set up a new game. The game
    //          will run in the given gui.
    public PreGameDialog(TetrisGui gui) {
        this.gui = gui;

        setModal(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initComponents();

        setResizable(false);
        setTitle("Tetris Instructions and Setup");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes and adds this dialog's components to this
    private void initComponents() {
        addInstructionsPanel();
        addComboBoxPanel();
        addStartGameButtonPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the panel containing the instructions to the dialog window
    private void addInstructionsPanel() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        instructionsPanel.add(new JLabel("Keyboard controls:"));
        instructionsPanel.add(new JLabel("UP: rotate 90 degrees clockwise"));
        instructionsPanel.add(new JLabel("DOWN: move down one row"));
        instructionsPanel.add(new JLabel("LEFT: move left one column"));
        instructionsPanel.add(new JLabel("RIGHT: move right one column"));
        instructionsPanel.add(new JLabel("SPACE: hard drop the piece"));
        instructionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        instructionsPanel.add(new JLabel("Choose the starting level of the game."));

        add(instructionsPanel);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the panel containing the combo box (and the JLabel beside it)
    //          to the dialog window
    private void addComboBoxPanel() {
        Integer[] levels = new Integer[Game.MAXIMUM_STARTING_LEVEL + 1];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = i;
        }

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("Starting level: ");
        comboBoxPanel.add(label);

        levelOptions = new JComboBox<Integer>(levels);
        comboBoxPanel.add(levelOptions);

        add(comboBoxPanel);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the panel containing the start game button to the dialog window
    private void addStartGameButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton startGameButton = new JButton("Start Game!");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreGameDialog.this.dispose();
                // The code for getting the selected combo box option
                // comes from https://stackoverflow.com/a/7026724/3335320
                gui.startNewGame(levelOptions.getItemAt(levelOptions.getSelectedIndex()));
            }
        });

        buttonPanel.add(startGameButton);
        add(buttonPanel);
    }
}
