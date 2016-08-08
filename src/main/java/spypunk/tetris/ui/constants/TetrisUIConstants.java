/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.constants;

import java.awt.Color;

public class TetrisUIConstants {

    public static final int BLOCK_SIZE = 32;

    public static final float DEFAULT_FONT_SIZE = 30F;

    public static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;

    public static final Color DEFAULT_CONTAINER_COLOR = Color.GRAY;

    private TetrisUIConstants() {
        throw new IllegalAccessError();
    }
}
