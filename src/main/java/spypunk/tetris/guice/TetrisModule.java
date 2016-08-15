/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.guice;

import com.google.inject.AbstractModule;

import spypunk.tetris.factory.GameLoopFactory;
import spypunk.tetris.factory.GameLoopFactoryImpl;
import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.factory.ShapeFactoryImpl;
import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.factory.TetrisFactoryImpl;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.service.TetrisServiceImpl;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.sound.service.SoundServiceImpl;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.TetrisControllerImpl;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandlerImpl;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandlerImpl;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.factory.FontFactoryImpl;
import spypunk.tetris.ui.factory.ImageFactory;
import spypunk.tetris.ui.factory.ImageFactoryImpl;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactoryImpl;
import spypunk.tetris.ui.factory.TetrisControllerInputTranslatorFactory;
import spypunk.tetris.ui.factory.TetrisControllerInputTranslatorFactoryImpl;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.factory.TetrisViewFactoryImpl;

public class TetrisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TetrisService.class).to(TetrisServiceImpl.class);
        bind(ShapeFactory.class).to(ShapeFactoryImpl.class);
        bind(TetrisController.class).to(TetrisControllerImpl.class);
        bind(TetrisViewFactory.class).to(TetrisViewFactoryImpl.class);
        bind(ImageFactory.class).to(ImageFactoryImpl.class);
        bind(TetrisFactory.class).to(TetrisFactoryImpl.class);
        bind(FontFactory.class).to(FontFactoryImpl.class);
        bind(GameLoopFactory.class).to(GameLoopFactoryImpl.class);
        bind(TetrisControllerInputHandler.class).to(TetrisControllerInputHandlerImpl.class);
        bind(TetrisControllerCommandFactory.class).to(TetrisControllerCommandFactoryImpl.class);
        bind(TetrisControllerInputTranslatorFactory.class).to(TetrisControllerInputTranslatorFactoryImpl.class);
        bind(TetrisControllerTetrisEventHandler.class).to(TetrisControllerTetrisEventHandlerImpl.class);
        bind(SoundService.class).to(SoundServiceImpl.class);
    }
}
