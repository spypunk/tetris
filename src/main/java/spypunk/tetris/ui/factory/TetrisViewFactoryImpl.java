/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.view.TetrisView;
import spypunk.tetris.ui.view.TetrisViewImpl;

@Singleton
public class TetrisViewFactoryImpl implements TetrisViewFactory {

    private final TetrisController tetrisController;

    private final FontCache fontCache;

    private final ImageCache imageCache;

    @Inject
    public TetrisViewFactoryImpl(final TetrisController tetrisController, final FontCache fontCache,
            final ImageCache imageCache) {
        this.tetrisController = tetrisController;
        this.fontCache = fontCache;
        this.imageCache = imageCache;
    }

    @Override
    public TetrisView createTetrisView(final Tetris tetris) {
        return new TetrisViewImpl(tetrisController, fontCache, imageCache, tetris);
    }
}
