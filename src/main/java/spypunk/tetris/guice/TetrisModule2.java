/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.guice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

import spypunk.tetris.Main;
import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.factory.ShapeFactoryImpl;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.service.TetrisServiceImpl;
import spypunk.tetris.sound.cache.SoundClipCache;
import spypunk.tetris.sound.cache.SoundClipCacheImpl;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.sound.service.SoundServiceImpl;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.cache.ImageCacheImpl;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.controller.TetrisControllerImpl;
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCache;
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCacheImpl;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandlerImpl;
import spypunk.tetris.ui.controller.gameloop.TetrisControllerGameLoop;
import spypunk.tetris.ui.controller.gameloop.TetrisControllerGameLoopImpl;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandlerImpl;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.font.cache.FontCacheImpl;
import spypunk.tetris.ui.view.TetrisMainView;
import spypunk.tetris.ui.view.TetrisMainView2Impl;
import spypunk.tetris.ui.view.TetrisMainViewImpl;

public class TetrisModule2 extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String NAME_KEY = "name";

    private static final String VERSION_KEY = "version";

    private static final String URL_KEY = "url";

    private static final String TETRIS_PROPERTIES = "/tetris.properties";

    private final Tetris tetris;


    public TetrisModule2() {
        String name;
        String version;
        URI uri;

        try (InputStream inputStream = TetrisModule.class.getResource(TETRIS_PROPERTIES).openStream()) {
            final Properties properties = new Properties();

            properties.load(inputStream);

            name = properties.getProperty(NAME_KEY);
            version = properties.getProperty(VERSION_KEY);
            uri = URI.create(properties.getProperty(URL_KEY));
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }

        tetris = new Tetris(name, version, uri);

    }

    @Override
    protected void configure() {
        bind(TetrisService.class).to(TetrisServiceImpl.class);
        bind(ShapeFactory.class).to(ShapeFactoryImpl.class);
        bind(TetrisController.class).to(TetrisControllerImpl.class);
        bind(ImageCache.class).to(ImageCacheImpl.class);
        bind(FontCache.class).to(FontCacheImpl.class);
        bind(TetrisControllerCommandCache.class).to(TetrisControllerCommandCacheImpl.class);
        bind(SoundService.class).to(SoundServiceImpl.class);
        bind(SoundClipCache.class).to(SoundClipCacheImpl.class);
        bind(TetrisControllerInputHandler.class).to(TetrisControllerInputHandlerImpl.class);
        bind(TetrisControllerTetrisEventHandler.class).to(TetrisControllerTetrisEventHandlerImpl.class);
        bind(TetrisControllerGameLoop.class).to(TetrisControllerGameLoopImpl.class);
        bind(TetrisMainView.class).to(TetrisMainView2Impl.class);
    }

    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
    @BindingAnnotation
    public @interface TetrisProvider {
    }

    @Provides
    @TetrisProvider
    @Inject
    public Tetris getTetris() {
        return tetris;
    }
}
