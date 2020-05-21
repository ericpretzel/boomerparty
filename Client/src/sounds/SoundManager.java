package sounds;

import util.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class SoundManager {
    public static final String INVALID_WORD_SOUND = "invalid_word.wav";
    public static final String VALID_WORD_SOUND = "valid_word.wav";
    public static final String BONUS_SOUND = "bonus.wav";
    public static final String LOSE_SOUND = "lose.wav";

    private SoundManager() {
    }

    public static void playSound(String path) {
        try {
            BufferedInputStream sound = new BufferedInputStream(Objects.requireNonNull(SoundManager.class.getClassLoader().getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            Logger.log("error occurred when trying to play \"" + path + "\"");
            e.printStackTrace();
        }
    }
}
