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

    public static final int STATISTICS_CONTAINER_X = 0;

    public static final int STATISTICS_CONTAINER_Y = 3 * BLOCK_SIZE;

    public static final int STATISTICS_CONTAINER_WIDTH = BLOCK_SIZE * 6;

    public static final int STATISTICS_CONTAINER_HEIGHT = 1 + BLOCK_SIZE * 15;

    public static final int TETRIS_CONTAINER_X = STATISTICS_CONTAINER_X + STATISTICS_CONTAINER_WIDTH + BLOCK_SIZE;

    public static final int TETRIS_CONTAINER_Y = 0;

    public static final int TETRIS_CONTAINER_WIDTH = 1 + BLOCK_SIZE * WIDTH;

    public static final int TETRIS_CONTAINER_HEIGHT = 1 + BLOCK_SIZE * (HEIGHT - 2);

    public static final int INFO_CONTAINERS_X = TETRIS_CONTAINER_X + TETRIS_CONTAINER_WIDTH + BLOCK_SIZE;

    public static final int INFO_CONTAINERS_WIDTH = BLOCK_SIZE * 6;

    public static final int LEVEL_CONTAINER_Y = BLOCK_SIZE * 3;

    public static final int LEVEL_CONTAINER_HEIGHT = BLOCK_SIZE;

    public static final int SCORE_CONTAINER_Y = LEVEL_CONTAINER_Y + LEVEL_CONTAINER_HEIGHT + 2 * BLOCK_SIZE;

    public static final int SCORE_CONTAINER_HEIGHT = BLOCK_SIZE;

    public static final int ROWS_CONTAINER_Y = SCORE_CONTAINER_Y + SCORE_CONTAINER_HEIGHT + 2 * BLOCK_SIZE;

    public static final int ROWS_CONTAINER_HEIGHT = BLOCK_SIZE;

    public static final int NEXT_SHAPE_CONTAINER_Y = ROWS_CONTAINER_Y + ROWS_CONTAINER_HEIGHT + 2 * BLOCK_SIZE;

    public static final int NEXT_SHAPE_CONTAINER_HEIGHT = BLOCK_SIZE * 6;

    public static final Dimension VIEW_DIMENSION = new Dimension(
            INFO_CONTAINERS_X + INFO_CONTAINERS_WIDTH + 1,
            TETRIS_CONTAINER_Y + TETRIS_CONTAINER_HEIGHT + 1);

    public static final String STATISTICS = "STATISTICS";

    public static final String SCORE = "SCORE";

    public static final String LEVEL = "LEVEL";

    public static final String NEXT_SHAPE = "NEXT";

    public static final String ROWS = "ROWS";

    public static final String GAME_OVER = "GAME OVER";

    public static final String PAUSE = "PAUSE";

    public static final float DEFAULT_FONT_SIZE = 30F;

    public static final float TETRIS_FROZEN_FONT_SIZE = 42F;

    public static final Color TETRIS_FROZEN_FG_COLOR = new Color(30, 30, 30, 200);

    public static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;

    public static final Color DEFAULT_CONTAINER_COLOR = Color.GRAY;

    public static final Color DEFAULT_URL_COLOR = Color.LIGHT_GRAY;

    private TetrisUIConstants() {
        throw new IllegalAccessError();
    }
}
