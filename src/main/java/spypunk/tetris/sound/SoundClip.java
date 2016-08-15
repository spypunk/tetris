package spypunk.tetris.sound;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import org.apache.commons.io.IOUtils;

import spypunk.tetris.exception.TetrisException;

public class SoundClip implements AutoCloseable {

    private final AudioFormat audioFormat;

    private final Clip clip;

    private final AudioInputStream audioInputStream;

    private final boolean loop;

    private final FloatControl volumeControl;

    private boolean paused;

    private long currentFramePosition;

    private boolean muted;

    private float currentVolume;

    public SoundClip(AudioInputStream inputStream, boolean loop) {
        audioFormat = getOutFormat(inputStream.getFormat());
        this.loop = loop;

        try {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(audioFormat, inputStream);
            clip.open(audioInputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (LineUnavailableException | IOException e) {
            throw new TetrisException(e);
        }
    }

    public void play() {
        resume();
    }

    public void pause() {
        if (paused) {
            resume();
        } else {
            currentFramePosition = clip.getMicrosecondPosition();

            clip.stop();

            paused = true;
        }
    }

    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public void mute() {
        muted = !muted;

        if (muted) {
            pause();
            volumeControl.setValue(volumeControl.getMinimum());
            pause();
        } else {
            volumeControl.setValue(currentVolume);
        }
    }

    @Override
    public void close() {
        clip.close();
        IOUtils.closeQuietly(audioInputStream);
    }

    private void resume() {
        clip.setMicrosecondPosition(currentFramePosition);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }

        paused = false;
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }
}
