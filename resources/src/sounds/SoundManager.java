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

    private SoundManager() {
    }

    public static void playSound(String path) {
        if (path.isEmpty())
            return;
        try {
            BufferedInputStream sound = new BufferedInputStream(Objects.requireNonNull(SoundManager.class.getResourceAsStream(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            Logger.log("could not play sound \"" + path + "\"");
            e.printStackTrace();
        }
    }
}
