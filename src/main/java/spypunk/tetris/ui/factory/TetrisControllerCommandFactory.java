/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerCommandFactory {

    TetrisControllerCommand createNewGameTetrisControllerCommand();

    TetrisControllerCommand createPauseTetrisControllerCommand();

    TetrisControllerCommand createMovementTetrisControllerCommand(Movement movement);

    TetrisControllerCommand createShapeLockedTetrisControllerCommand();

    TetrisControllerCommand createMuteTetrisControllerCommand();

    TetrisControllerCommand createGameOverTetrisControllerCommand();

    TetrisControllerCommand createRowsCompletedTetrisControllerCommand();

    TetrisControllerCommand createIncreaseVolumeTetrisControllerCommand();

    TetrisControllerCommand createDecreaseVolumeTetrisControllerCommand();

    TetrisControllerCommand createHardDropTetrisControllerCommand();
}
