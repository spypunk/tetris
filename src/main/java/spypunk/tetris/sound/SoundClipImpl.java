/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
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

    private final Clip clip;

    private final boolean loop;

    private final FloatControl volumeControl;

    private long currentFramePosition;

    private float minimumVolume;

    private float maximumVolume;

    private float currentVolume;

    public SoundClipImpl(final AudioInputStream inputStream, final boolean loop) {
        this.loop = loop;

        try {
            clip = AudioSystem.getClip();

            final AudioFormat audioFormat = getOutFormat(inputStream.getFormat());
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
                inputStream);

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
        clip.setMicrosecondPosition(currentFramePosition);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    @Override
    public void pause() {
        currentFramePosition = clip.getMicrosecondPosition();

        clip.stop();
    }

    @Override
    public void stop() {
        clip.stop();
        currentFramePosition = 0;
    }

    @Override
    public void mute() {
        volumeControl.setValue(minimumVolume);
    }

    @Override
    public void unMute() {
        volumeControl.setValue(currentVolume);
    }

    @Override
    public void increaseVolume() {
        updateVolume(VOLUME_DELTA);
    }

    @Override
    public void decreaseVolume() {
        updateVolume(-VOLUME_DELTA);
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

    private AudioFormat getOutFormat(final AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }
}
