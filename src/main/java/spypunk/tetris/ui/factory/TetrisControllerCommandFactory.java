package spypunk.tetris.ui.factory;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerCommandFactory {

    TetrisControllerCommand createNewGameTetrisControllerCommand();

    TetrisControllerCommand createPauseTetrisControllerCommand();

    TetrisControllerCommand createMovementTetrisControllerCommand(Movement movement);
}
