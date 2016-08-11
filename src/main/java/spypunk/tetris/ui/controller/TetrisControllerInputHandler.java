package spypunk.tetris.ui.controller;

import java.util.List;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerInputHandler {

    void onMovement(Movement movement);

    void onPause();

    void onNewGame();

    boolean isMovementTriggered();

    boolean isPauseTriggered();

    boolean isNewGameTriggered();

    Movement getMovement();

    void reset();

    List<TetrisControllerCommand> handleInput();
}
