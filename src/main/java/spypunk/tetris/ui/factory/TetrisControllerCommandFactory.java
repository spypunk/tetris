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

    TetrisControllerCommand createNewGameCommand();

    TetrisControllerCommand createPauseCommand();

    TetrisControllerCommand createMoveCommand(Movement movement);

    TetrisControllerCommand createShapeLockedCommand();

    TetrisControllerCommand createMuteCommand();

    TetrisControllerCommand createGameOverCommand();

    TetrisControllerCommand createRowsCompletedCommand();

    TetrisControllerCommand createIncreaseVolumeCommand();

    TetrisControllerCommand createDecreaseVolumeCommand();

    TetrisControllerCommand createHardDropCommand();

    TetrisControllerCommand createOpenProjectURLCommand();
}
