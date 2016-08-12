package spypunk.tetris.ui.factory;

import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputTranslator;

@FunctionalInterface
public interface TetrisControllerInputTranslatorFactory {

    TetrisControllerInputTranslator createTetrisControllerInputTranslator(
            TetrisControllerInputHandler tetrisControllerInputHandler);
}
