/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.sound.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.SoundClip;

@Singleton
public class SoundServiceImpl implements SoundService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoundServiceImpl.class);

    private static final String SOUNDS_FOLDER = "/sound/";

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<Sound, SoundClip> soundClips = createSoundClips();

    private SoundClip currentSoundClip;

    private boolean muted;

    @Override
    public void shutdown() {
        soundClips.values().forEach(SoundClip::close);
        executorService.shutdown();
    }

    @Override
    public void playMusic(Sound sound) {
        if (muted) {
            return;
        }

        executorService.execute(() -> doPlayMusic(sound));
    }

    @Override
    public void pauseMusic() {
        executorService.execute(this::doPauseMusic);
    }

    @Override
    public void stopMusic() {
        if (currentSoundClip != null) {
            currentSoundClip.stop();
            currentSoundClip = null;
        }
    }

    @Override
    public void playSound(Sound sound) {
        if (muted) {
            return;
        }

        executorService.execute(() -> doPlaySound(sound));
    }

    private static Map<Sound, SoundClip> createSoundClips() {
        final Map<Sound, SoundClip> soundClips = Maps.newHashMap();

        for (final Sound sound : Sound.values()) {
            soundClips.put(sound, createSoundClip(sound));
        }

        return soundClips;
    }

    private static SoundClip createSoundClip(Sound sound) {
        try (InputStream inputStream = SoundServiceImpl.class
                .getResourceAsStream(SOUNDS_FOLDER + sound.getFileName());
                AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(new BufferedInputStream(inputStream))) {

            return new SoundClip(audioInputStream, sound.isLoop());
        } catch (UnsupportedAudioFileException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private void doPlaySound(Sound sound) {
        final SoundClip clip = soundClips.get(sound);

        clip.stop();
        clip.play();
    }

    private void doPlayMusic(Sound sound) {
        stopMusic();

        final SoundClip clip = soundClips.get(sound);

        currentSoundClip = clip;

        clip.play();
    }

    private void doPauseMusic() {
        if (currentSoundClip != null) {
            currentSoundClip.pause();
        }
    }

    @Override
    public void mute() {
        muted = !muted;
    }
}
