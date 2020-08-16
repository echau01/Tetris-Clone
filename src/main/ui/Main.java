package ui;

import ui.graphics.TetrisGui;

import javax.swing.*;
import java.io.File;

// This is the class that the main method is located in.
public class Main {
    public static void main(String[] args) {
        // This try-catch block comes from https://stackoverflow.com/a/2076309/3335320 and
        // https://stackoverflow.com/a/9682048/3335320. It makes text look nicer on different computers.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If we export the application to a JAR file and put it in a directory that does not have a data
        // folder, the application must create a data folder in order to save the user's scores.
        if (!makeDataFolder()) {
            return;
        }

        // See https://stackoverflow.com/a/3551578/3335320 for why we need to run the GUI
        // using SwingUtilities.invokeLater.
        // We want to run the GUI on the event dispatching thread to avoid the problem of
        // closing the app while we are saving scoreboard data to file.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGui();
            }
        });
    }

    // EFFECTS: makes a data folder in the directory that this program was launched in.
    //          Returns false if the folder does not exist and cannot be created, or if a SecurityException is caught.
    //          In both of these cases, a dialog window appears telling the user what happened.
    //          Otherwise, returns true.
    private static boolean makeDataFolder() {
        // I got the code for making a data folder from https://stackoverflow.com/a/3634879/3335320.
        File folder = new File("./data");
        boolean folderExists = folder.exists();
        try {
            boolean folderCreated = folder.mkdirs();
            if (!folderExists && !folderCreated) {
                JOptionPane.showMessageDialog(null, "Failed to create data folder at:\n"
                        + folder.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Caught SecurityException when trying to create "
                    + "data folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
