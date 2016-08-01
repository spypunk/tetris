/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.guice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.AbstractModule;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.factory.ShapeFactoryImpl;
import spypunk.tetris.factory.ShapeTypeFactory;
import spypunk.tetris.factory.ShapeTypeFactoryImpl;
import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.factory.TetrisFactoryImpl;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.service.TetrisServiceImpl;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.TetrisControllerImpl;
import spypunk.tetris.ui.factory.BlockImageFactory;
import spypunk.tetris.ui.factory.BlockImageFactoryImpl;
import spypunk.tetris.ui.factory.ContainerFactory;
import spypunk.tetris.ui.factory.ContainerFactoryImpl;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.factory.FontFactoryImpl;
import spypunk.tetris.ui.factory.ImageFactory;
import spypunk.tetris.ui.factory.ImageFactoryImpl;
import spypunk.tetris.ui.view.TetrisFrame;
import spypunk.tetris.ui.view.TetrisFrameImpl;
import spypunk.tetris.ui.view.TetrisRenderer;
import spypunk.tetris.ui.view.TetrisRendererImpl;

public class TetrisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
        bind(TetrisService.class).to(TetrisServiceImpl.class);
        bind(ShapeFactory.class).to(ShapeFactoryImpl.class);
        bind(TetrisController.class).to(TetrisControllerImpl.class);
        bind(TetrisFrame.class).to(TetrisFrameImpl.class);
        bind(TetrisRenderer.class).to(TetrisRendererImpl.class);
        bind(ImageFactory.class).to(ImageFactoryImpl.class);
        bind(BlockImageFactory.class).to(BlockImageFactoryImpl.class);
        bind(ShapeTypeFactory.class).to(ShapeTypeFactoryImpl.class);
        bind(TetrisFactory.class).to(TetrisFactoryImpl.class);
        bind(FontFactory.class).to(FontFactoryImpl.class);
        bind(ContainerFactory.class).to(ContainerFactoryImpl.class);
    }
}
