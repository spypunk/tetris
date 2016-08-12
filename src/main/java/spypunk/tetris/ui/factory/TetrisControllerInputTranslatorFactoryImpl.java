/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

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
