package ui;

import javax.swing.*;

// This is the class that the main method is located in.
public class Main {
    public static void main(String[] args) {
        // See https://stackoverflow.com/a/3551578/3335320 for why we need to run the GUI
        // using SwingUtilities.invokeLater.
        // We want to run the GUI on the event dispatching thread to avoid the problem of
        // closing the app while we are saving scoreboard data to file.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI();
            }
        });
    }
}
