package ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// This class plays the Tetris theme song on loop.
public class ThemeSongPlayer {
    // The music file comes from https://commons.wikimedia.org/wiki/File:Tetris_theme.ogg (CC BY-SA 3.0).
    // The file was originally an MP3 file. I converted the MP3 to WAV using https://online-audio-converter.com/.
    private static final String THEME_SONG_FILE_PATH = "./data/tetrisTheme.wav";

    // This class uses the singleton pattern. This is the singleton instance.
    private static ThemeSongPlayer instance;

    private Clip clip;

    // EFFECTS: makes a new ThemeSongPlayer
    private ThemeSongPlayer() {}

    // MODIFIES: this
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
        // The code for this method comes from https://stackoverflow.com/a/953752/3335320
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(THEME_SONG_FILE_PATH));
        clip = AudioSystem.getClip();
        clip.open(stream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-15.0f);
        clip.start();
    }

    // MODIFIES: this
    // EFFECTS: stops playing the theme song
    public void stop() {
        clip.stop();
        clip.close();
    }
}
