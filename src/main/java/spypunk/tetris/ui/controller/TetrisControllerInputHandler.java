package spypunk.tetris.ui.controller;

import java.util.List;

import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerInputHandler {

    void onKeyPressed(int keyCode);

    void onKeyReleased(int keyCode);

    void reset();

    List<TetrisControllerCommand> handleInput();
}
