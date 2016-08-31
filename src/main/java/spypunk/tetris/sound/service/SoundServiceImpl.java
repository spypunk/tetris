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

    private SoundClip currentMusicSoundClip;

    @Inject
    public SoundServiceImpl(final SoundClipCache soundClipCache) {
        this.soundClipCache = soundClipCache;
    }

    @Override
    public void playMusic(final Sound sound) {
        doPlayMusic(sound);
    }

    @Override
    public void pauseMusic() {
        doPauseMusic();
    }

    @Override
    public void stopMusic() {
        if (currentMusicSoundClip != null) {
            currentMusicSoundClip.stop();
            currentMusicSoundClip = null;
        }
    }

    @Override
    public void playSound(final Sound sound) {
        doPlaySound(sound);
    }

    @Override
    public void mute() {
        soundClipCache.getAllSoundClips().forEach(SoundClip::mute);
    }

    @Override
    public void increaseVolume() {
        soundClipCache.getAllSoundClips().forEach(SoundClip::increaseVolume);
    }

    @Override
    public void decreaseVolume() {
        soundClipCache.getAllSoundClips().forEach(SoundClip::decreaseVolume);
    }

    @Override
    public boolean isMute() {
        return soundClipCache.getAllSoundClips().stream().allMatch(SoundClip::isMute);
    }

    private void doPlaySound(final Sound sound) {
        final SoundClip clip = soundClipCache.getSoundClip(sound);

        clip.stop();
        clip.play();
    }

    private void doPlayMusic(final Sound sound) {
        stopMusic();

        currentMusicSoundClip = soundClipCache.getSoundClip(sound);

        currentMusicSoundClip.play();
    }

    private void doPauseMusic() {
        if (currentMusicSoundClip != null) {
            currentMusicSoundClip.pause();
        }
    }
}
