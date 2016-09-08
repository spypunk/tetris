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
import spypunk.tetris.service.TetrisInstanceService;
import spypunk.tetris.service.TetrisInstanceServiceImpl;
import spypunk.tetris.sound.cache.SoundClipCache;
import spypunk.tetris.sound.cache.SoundClipCacheImpl;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.sound.service.SoundServiceImpl;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.cache.ImageCacheImpl;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.TetrisControllerImpl;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandlerImpl;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandlerImpl;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactoryImpl;
import spypunk.tetris.ui.factory.TetrisViewFactory;
import spypunk.tetris.ui.factory.TetrisViewFactoryImpl;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.font.cache.FontCacheImpl;

public class TetrisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TetrisInstanceService.class).to(TetrisInstanceServiceImpl.class);
        bind(ShapeFactory.class).to(ShapeFactoryImpl.class);
        bind(TetrisController.class).to(TetrisControllerImpl.class);
        bind(TetrisViewFactory.class).to(TetrisViewFactoryImpl.class);
        bind(ImageCache.class).to(ImageCacheImpl.class);
        bind(TetrisFactory.class).to(TetrisFactoryImpl.class);
        bind(FontCache.class).to(FontCacheImpl.class);
        bind(GameLoopFactory.class).to(GameLoopFactoryImpl.class);
        bind(TetrisControllerCommandFactory.class).to(TetrisControllerCommandFactoryImpl.class);
        bind(SoundService.class).to(SoundServiceImpl.class);
        bind(SoundClipCache.class).to(SoundClipCacheImpl.class);
        bind(TetrisControllerInputHandler.class).to(TetrisControllerInputHandlerImpl.class);
        bind(TetrisControllerTetrisEventHandler.class).to(TetrisControllerTetrisEventHandlerImpl.class);
    }
}
