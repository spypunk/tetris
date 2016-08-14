/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.service;

public enum Sound {

    SHAPE_LOCKED(Extension.WAV);

    private enum Extension {
        WAV
    }

    private final Extension extension;

    private Sound(Extension extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return name() + "." + extension.name().toLowerCase();
    }
}
