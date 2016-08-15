/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

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

public class SoundClipImpl implements SoundClip {

    private final AudioFormat audioFormat;

    private final Clip clip;

    private final AudioInputStream audioInputStream;

    private final boolean loop;

    private final FloatControl volumeControl;

    private boolean paused;

    private long currentFramePosition;

    private boolean muted;

    private float currentVolume;

    public SoundClipImpl(AudioInputStream inputStream, boolean loop) {
        audioFormat = getOutFormat(inputStream.getFormat());
        this.loop = loop;

        try {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(audioFormat, inputStream);
            clip.open(audioInputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (LineUnavailableException | IOException e) {
            close();
            throw new TetrisException(e);
        }
    }

    @Override
    public void play() {
        resume();
    }

    @Override
    public void pause() {
        if (paused) {
            resume();
        } else {
            currentFramePosition = clip.getMicrosecondPosition();

            clip.stop();

            paused = true;
        }
    }

    @Override
    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    @Override
    public void mute() {
        muted = !muted;

        final boolean pausedNeeded = !paused;

        if (pausedNeeded) {
            pause();
        }

        if (muted) {
            volumeControl.setValue(volumeControl.getMinimum());
        } else {
            volumeControl.setValue(currentVolume);
        }

        if (pausedNeeded) {
            pause();
        }
    }

    @Override
    public void close() {
        if (clip != null) {
            clip.close();
        }

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
