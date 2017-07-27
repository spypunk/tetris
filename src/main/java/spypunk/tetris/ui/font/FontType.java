/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.font;

public enum FontType {

    DEFAULT(30F, Font.NEUTRONIUM),
    URL(10F, Font.RUSSO_ONE),
    FROZEN(42F, Font.NEUTRONIUM);

    private final float size;

    private final Font font;

    FontType(final float size, final Font font) {
        this.size = size;
        this.font = font;
    }

    public float getSize() {
        return size;
    }

    public String getFileName() {
        return font.getFileName();
    }
}
