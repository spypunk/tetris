package spypunk.tetris.ui.factory;

import javax.inject.Singleton;

import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputTranslator;
import spypunk.tetris.ui.controller.input.TetrisControllerInputTranslatorImpl;

@Singleton
public class TetrisControllerInputTranslatorFactoryImpl implements TetrisControllerInputTranslatorFactory {

    @Override
    public TetrisControllerInputTranslator createTetrisControllerInputTranslator(
            TetrisControllerInputHandler tetrisControllerInputHandler) {
        return new TetrisControllerInputTranslatorImpl(tetrisControllerInputHandler);
    }

}
