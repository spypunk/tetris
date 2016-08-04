/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.constants;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;

import java.awt.Color;
import java.awt.Dimension;

public class TetrisUIConstants {

    public static final int BLOCK_SIZE = 32;

    public static final String TITLE = "Tetris";

    public static final Dimension DEFAULT_DIMENSION = new Dimension(
            WIDTH * BLOCK_SIZE + 16 * BLOCK_SIZE,
            (HEIGHT - 2) * BLOCK_SIZE + 2 * BLOCK_SIZE);

    public static final float DEFAULT_FONT_SIZE = 30F;

    public static final float TETRIS_FROZEN_FONT_SIZE = 42F;

    public static final Color TETRIS_FROZEN_FG_COLOR = new Color(30, 30, 30, 150);

    public static final String GAME_OVER = "GAME OVER";

    public static final String PAUSE = "PAUSE";

    public static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;

    public static final Color DEFAULT_CONTAINER_COLOR = Color.GRAY;

    public static final int INFO_CONTAINERS_WIDTH = BLOCK_SIZE * 6;

    public static final int TETRIS_CONTAINER_WIDTH = 1 + BLOCK_SIZE * WIDTH;

    public static final int STATISTICS_CONTAINERS_WIDTH = BLOCK_SIZE * 6;

    public static final int STATISTICS_CONTAINERS_X = BLOCK_SIZE;

    public static final int TETRIS_CONTAINERS_X = STATISTICS_CONTAINERS_X + STATISTICS_CONTAINERS_WIDTH + BLOCK_SIZE;

    public static final int INFO_CONTAINERS_X = TETRIS_CONTAINERS_X + TETRIS_CONTAINER_WIDTH + BLOCK_SIZE;

    public static final int STATISTICS_CONTAINER_HEIGHT = 1 + BLOCK_SIZE * (HEIGHT - 3);

    public static final int TETRIS_CONTAINER_HEIGHT = 1 + BLOCK_SIZE * (HEIGHT - 2);

    public static final int ROWS_CONTAINER_Y = BLOCK_SIZE * 8;

    public static final int SCORE_CONTAINER_Y = BLOCK_SIZE * 5;

    public static final int LEVEL_CONTAINER_Y = BLOCK_SIZE * 2;

    public static final int NEXT_SHAPE_CONTAINER_HEIGHT = BLOCK_SIZE * 4;

    public static final int NEXT_SHAPE_CONTAINER_Y = BLOCK_SIZE * 11;

    public static final String STATISTICS = "STATISTICS";

    public static final String SCORE = "SCORE";

    public static final String LEVEL = "LEVEL";

    public static final String NEXT_SHAPE = "NEXT";

    public static final String ROWS = "ROWS";

    private TetrisUIConstants() {
        throw new IllegalAccessError();
    }
}
