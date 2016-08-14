/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spypunk.tetris.exception.TetrisException;

@Singleton
public class SoundServiceImpl implements SoundService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoundServiceImpl.class);

    private static final String SOUNDS_FOLDER = "/sound/";

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<Sound, Clip> soundClips = createSoundClips();

    private boolean muted;

    @Override
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void playMusic() {
        // TODO
    }

    @Override
    public void pauseMusic() {
        // TODO
    }

    @Override
    public void stopMusic() {
        // TODO
    }

    @Override
    public void playSound(Sound sound) {
        if (muted) {
            return;
        }

        executorService.execute(() -> doPlaySound(sound));
    }

    private static Map<Sound, Clip> createSoundClips() {
        final Map<Sound, Clip> soundClips = Maps.newHashMap();

        for (final Sound sound : Sound.values()) {
            soundClips.put(sound, createSoundClip(sound));
        }

        return soundClips;
    }

    private static Clip createSoundClip(Sound sound) {
        try (InputStream inputStream = SoundServiceImpl.class.getResourceAsStream(SOUNDS_FOLDER + sound.getFileName());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream)) {
            final DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            final Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private void doPlaySound(Sound sound) {
        final Clip clip = soundClips.get(sound);

        if (clip.isRunning()) {
            clip.drain();
            clip.stop();
        }

        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void mute() {
        muted = !muted;
    }
}
