/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.sound.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.SoundClip;
import spypunk.tetris.sound.cache.SoundClipCache;

@Singleton
public class SoundServiceImpl implements SoundService {

    private final SoundClipCache soundClipCache;

    private SoundClip currentSoundClip;

    private boolean muted;

    @Inject
    public SoundServiceImpl(SoundClipCache soundClipCache) {
        this.soundClipCache = soundClipCache;
    }

    @Override
    public void playMusic(Sound sound) {
        if (muted) {
            return;
        }

        doPlayMusic(sound);
    }

    @Override
    public void pauseMusic() {
        doPauseMusic();
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

        doPlaySound(sound);
    }

    @Override
    public void mute() {
        doMute();
    }

    private void doPlaySound(Sound sound) {
        final SoundClip clip = soundClipCache.getSoundClip(sound);

        clip.stop();
        clip.play();
    }

    private void doPlayMusic(Sound sound) {
        stopMusic();

        final SoundClip clip = soundClipCache.getSoundClip(sound);

        currentSoundClip = clip;

        clip.play();
    }

    private void doPauseMusic() {
        if (currentSoundClip != null) {
            currentSoundClip.pause();
        }
    }

    private void doMute() {
        muted = !muted;

        if (currentSoundClip != null) {
            currentSoundClip.mute();
        }
    }
}
