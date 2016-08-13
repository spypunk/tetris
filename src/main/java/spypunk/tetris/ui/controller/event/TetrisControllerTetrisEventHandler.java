package spypunk.tetris.ui.controller.event;

import java.util.List;

import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

public interface TetrisControllerTetrisEventHandler {

    List<TetrisControllerCommand> handleEvents(List<TetrisEvent> tetrisEvents);
}
