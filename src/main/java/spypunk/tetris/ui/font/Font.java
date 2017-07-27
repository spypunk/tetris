/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.font;

public enum Font {

    NEUTRONIUM,
    RUSSO_ONE;

    private static final String TTF_FORMAT = ".ttf";

    private final String fileName;

    Font() {
        fileName = name().toLowerCase() + TTF_FORMAT;
    }

    public String getFileName() {
        return fileName;
    }
}
