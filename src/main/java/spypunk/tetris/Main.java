/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris;

import com.google.inject.Guice;
import com.google.inject.Injector;

import spypunk.tetris.guice.TetrisModule;
import spypunk.tetris.ui.controller.TetrisController;

public class Main {

    private Main() {
        throw new IllegalAccessError();
    }

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new TetrisModule());
        final TetrisController tetrisController = injector.getInstance(TetrisController.class);
        tetrisController.start();
    }

}
