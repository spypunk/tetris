/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.factory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.Main;
import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;

@Singleton
public class TetrisFactoryImpl implements TetrisFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String NAME_KEY = "name";

    private static final String VERSION_KEY = "version";

    private static final String URL_KEY = "url";

    private static final String TETRIS_PROPERTIES = "/tetris.properties";

    private final String name;

    private final String version;

    private final URI uri;

    public TetrisFactoryImpl() {
        try (InputStream inputStream = TetrisFactoryImpl.class.getResource(TETRIS_PROPERTIES).openStream()) {
            final Properties properties = new Properties();

            properties.load(inputStream);

            name = properties.getProperty(NAME_KEY);
            version = properties.getProperty(VERSION_KEY);
            uri = URI.create(properties.getProperty(URL_KEY));
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    @Override
    public Tetris createTetris() {
        return Tetris.Builder.instance().setName(name).setVersion(version).setProjectURI(uri).setState(State.STOPPED)
                .build();
    }
}
