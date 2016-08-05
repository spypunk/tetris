/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.guice.TetrisModule;
import spypunk.tetris.ui.controller.TetrisController;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String VERSION_KEY = "version";

    private static final String URL_KEY = "url";

    private static final String PROJECT_PROPERTIES = "/project.properties";

    public static final String VERSION;

    public static final URI URL;

    static {
        try (InputStream inputStream = Main.class.getResource(PROJECT_PROPERTIES).openStream()) {
            final Properties properties = new Properties();

            properties.load(inputStream);

            VERSION = properties.getProperty(VERSION_KEY);
            URL = URI.create(properties.getProperty(URL_KEY));
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private Main() {
        throw new IllegalAccessError();
    }

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new TetrisModule());
        final TetrisController tetrisController = injector.getInstance(TetrisController.class);
        tetrisController.start();
    }

}
