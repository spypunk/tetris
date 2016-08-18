/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.font;

public enum FontType {

    DEFAULT(30F, FontConstants.NEUTRONIUM),
    URL(10F, FontConstants.RUSSO_ONE),
    FROZEN(42F, FontConstants.NEUTRONIUM);

    private final float size;

    private final String fileName;

    private FontType(final float size, final String fontName) {
        this.size = size;
        fileName = fontName + ".ttf";
    }

    public float getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }
}
