/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.service;

import spypunk.tetris.ui.sound.Sound;

public interface SoundService {

    void stop();

    void playSound(Sound sound);

    void playMusic(Sound sound);

    void pauseMusic();

    void stopMusic();

    void mute();
}
