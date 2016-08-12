package spypunk.tetris.ui.controller.input;

import java.util.List;

import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerInputHandler {

    void onMoveLeft();

    void onMoveRight();

    void onMoveDown();

    void onRotate();

    void onNewGame();

    void onPause();

    void reset();

    List<TetrisControllerCommand> handleInput();
}
