/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.guice.TetrisModule;
import spypunk.tetris.ui.controller.TetrisController;

public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
        throw new IllegalAccessError();
    }

    public static void main(final String[] args) {
        try {
            final Injector injector = Guice.createInjector(new TetrisModule());
            final TetrisController tetrisController = injector.getInstance(TetrisController.class);

            tetrisController.start();
        } catch (CreationException | ConfigurationException | ProvisionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

}
