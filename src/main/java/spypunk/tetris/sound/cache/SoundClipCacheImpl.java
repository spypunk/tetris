/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.sound.cache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.SoundClip;
import spypunk.tetris.sound.SoundClipImpl;
import spypunk.tetris.sound.service.SoundServiceImpl;

@Singleton
public class SoundClipCacheImpl implements SoundClipCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoundClipCacheImpl.class);

    private static final String SOUNDS_FOLDER = "/sound/";

    private final Map<Sound, SoundClip> soundClips = createSoundClips();

    @Override
    public SoundClip getSoundClip(final Sound sound) {
        return soundClips.get(sound);
    }

    @Override
    public Collection<SoundClip> getAllSoundClips() {
        return soundClips.values();
    }

    private static SoundClip createSoundClip(final Sound sound) {
        try (InputStream inputStream = SoundServiceImpl.class
                .getResourceAsStream(SOUNDS_FOLDER + sound.getFileName());
                AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(new BufferedInputStream(inputStream))) {

            return new SoundClipImpl(audioInputStream, sound.isLoop());
        } catch (UnsupportedAudioFileException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private static Map<Sound, SoundClip> createSoundClips() {
        return Arrays.asList(Sound.values()).stream()
                .collect(Collectors.toMap(Function.identity(), SoundClipCacheImpl::createSoundClip));
    }
}
