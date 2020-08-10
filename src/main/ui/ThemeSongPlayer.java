package ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// This class plays the Tetris theme song on loop. Much of the code for this class comes from
// https://stackoverflow.com/a/11025384/3335320
public class ThemeSongPlayer {
    // The music file comes from https://commons.wikimedia.org/wiki/File:Tetris_theme.ogg
    // I converted the MP3 to WAV.
    private static final String THEME_SONG_FILE_PATH = "./data/tetrisTheme.wav";

    // This class uses the singleton pattern
    private static ThemeSongPlayer instance;

    private Clip clip;

    private ThemeSongPlayer() {}

    // EFFECTS: returns the singleton instance of ThemeSongPlayer.
    public static ThemeSongPlayer getInstance() {
        if (instance == null) {
            instance = new ThemeSongPlayer();
        }
        return instance;
    }

    // MODIFIES: this
    // EFFECTS: starts playing the song at THEME_SONG_FILE_PATH on loop.
    //          Throws IOException if an I/O error occurs
    //          Throws LineUnavailableException if the "line" that plays the music is not available
    //          Throws UnsupportedAudioFileException if the file at THEME_SONG_FILE_PATH is not a valid audio file.
    public void startThemeOnLoop() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // The code for this method comes from https://stackoverflow.com/a/11025384/3335320
        File musicFile = new File(THEME_SONG_FILE_PATH);
        AudioInputStream stream = AudioSystem.getAudioInputStream(musicFile);
        AudioFormat format = stream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        clip = (Clip) AudioSystem.getLine(info);
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    // MODIFIES: this
    // EFFECTS: stops playing the theme song
    public void stop() {
        clip.stop();
        clip.close();
    }
}
