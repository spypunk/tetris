/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.sound;

public enum Sound {

    SHAPE_LOCKED(Extension.WAV),
    GAME_OVER(Extension.WAV);

    private enum Extension {
        WAV
    }

    private final String fileName;

    private Sound(Extension extension) {
        fileName = name().toLowerCase() + "." + extension.name().toLowerCase();
    }

    public String getFileName() {
        return fileName;
    }
}
