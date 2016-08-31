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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.exception.TetrisException;

public class SoundClipImpl implements SoundClip {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoundClipImpl.class);

    private static final float VOLUME_DELTA = 5F;

    private final AudioFormat audioFormat;

    private final Clip clip;

    private final AudioInputStream audioInputStream;

    private final boolean loop;

    private final FloatControl volumeControl;

    private boolean paused;

    private long currentFramePosition;

    private boolean mute;

    private float minimumVolume;

    private float maximumVolume;

    private float currentVolume;

    private boolean stopped = true;

    public SoundClipImpl(final AudioInputStream inputStream, final boolean loop) {
        audioFormat = getOutFormat(inputStream.getFormat());
        this.loop = loop;

        try {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(audioFormat, inputStream);
            clip.open(audioInputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            maximumVolume = volumeControl.getValue();
            minimumVolume = volumeControl.getMinimum();
            currentVolume = maximumVolume;
        } catch (LineUnavailableException | IOException e) {
            LOGGER.error(e.getMessage(), e);
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
        stopped = true;
        currentFramePosition = 0;
    }

    @Override
    public void mute() {
        mute = !mute;

        final boolean pausedNeeded = !stopped && !paused;

        if (pausedNeeded) {
            pause();
        }

        if (mute) {
            volumeControl.setValue(minimumVolume);
        } else {
            volumeControl.setValue(currentVolume);
        }

        if (pausedNeeded) {
            pause();
        }
    }

    @Override
    public void increaseVolume() {
        updateVolume(VOLUME_DELTA);
    }

    @Override
    public void decreaseVolume() {
        updateVolume(-VOLUME_DELTA);
    }

    @Override
    public boolean isMute() {
        return mute;
    }

    private void updateVolume(final float delta) {
        currentVolume += delta;

        if (currentVolume < minimumVolume) {
            currentVolume = minimumVolume;
        } else if (currentVolume > maximumVolume) {
            currentVolume = maximumVolume;
        }

        volumeControl.setValue(currentVolume);
    }

    private void resume() {
        clip.setMicrosecondPosition(currentFramePosition);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }

        paused = false;
        stopped = false;
    }

    private AudioFormat getOutFormat(final AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }
}
