package spypunk.tetris.ui.controller.command;

import spypunk.tetris.model.Tetris;

@FunctionalInterface
public interface TetrisControllerCommand {

    void execute(Tetris tetris);
}
