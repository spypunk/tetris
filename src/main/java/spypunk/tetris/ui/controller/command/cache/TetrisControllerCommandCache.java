/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.command.cache;

import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerCommandCache {

    public enum TetrisControllerCommandType {
        LEFT,
        RIGHT,
        DOWN,
        ROTATE,
        NEW_GAME,
        PAUSE,
        MUTE,
        DECREASE_VOLUME,
        INCREASE_VOLUME,
        HARD_DROP,
        OPEN_PROJECT_URL,
        SHAPE_LOCKED,
        GAME_OVER,
        ROWS_COMPLETED,
        SHOW_SCORES
    }

    TetrisControllerCommand getTetrisControllerCommand(TetrisControllerCommandType tetrisControllerCommandType);
}
