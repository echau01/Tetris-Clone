package ui;

import ui.graphics.TetrisGui;
import ui.util.TemporaryScoreboardManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.util.Enumeration;

// This is the class that the main method is located in.
public class Main {
    // This is the parent directory for all files the application needs to access (e.g. scoreboard files)
    public static final String TETRIS_DIRECTORY = System.getProperty("user.home") + "\\Tetris";

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

        try {
            new File(TemporaryScoreboardManager.ENTRIES_FILE_PATH).createNewFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not create file "
                    + TemporaryScoreboardManager.ENTRIES_FILE_PATH, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Code to scale font size according to screen size comes from https://stackoverflow.com/a/32550596/3335320
        setDefaultFont(new Font("Sans Serif", Font.PLAIN,
                Math.max(14, Toolkit.getDefaultToolkit().getScreenSize().width / 140)));

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

    // EFFECTS: sets the default font of all text in the program to given font
    private static void setDefaultFont(Font font) {
        // Code to set a default font comes from https://stackoverflow.com/a/7434935/3335320
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    // EFFECTS: creates a data folder inside the folder at TETRIS_DIRECTORY.
    //          Returns false if the folder does not exist and cannot be created, or if a SecurityException is caught.
    //          In both of these cases, a dialog window appears telling the user what happened.
    //          Otherwise, returns true.
    private static boolean makeDataFolder() {
        String directory = TETRIS_DIRECTORY + "\\data";

        // I got the code for making a data folder from https://stackoverflow.com/a/3634879/3335320.
        File folder = new File(directory);
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
