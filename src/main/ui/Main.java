package ui;

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
        // I got the code for making a data folder from https://stackoverflow.com/a/3634879/3335320.
        new File("./data").mkdirs();

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
}
