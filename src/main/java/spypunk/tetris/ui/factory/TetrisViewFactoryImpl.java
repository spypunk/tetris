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
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.view.TetrisInstanceInfoView;
import spypunk.tetris.ui.view.TetrisInstanceInfoViewImpl;
import spypunk.tetris.ui.view.TetrisInstanceStatisticsView;
import spypunk.tetris.ui.view.TetrisInstanceStatisticsViewImpl;
import spypunk.tetris.ui.view.TetrisInstanceView;
import spypunk.tetris.ui.view.TetrisInstanceViewImpl;
import spypunk.tetris.ui.view.TetrisView;
import spypunk.tetris.ui.view.TetrisViewImpl;

@Singleton
public class TetrisViewFactoryImpl implements TetrisViewFactory {

    private final TetrisController tetrisController;

    private final FontCache fontCache;

    private final ImageCache imageCache;

    private final SoundService soundService;

    @Inject
    public TetrisViewFactoryImpl(final TetrisController tetrisController, final FontCache fontCache,
            final ImageCache imageCache, final SoundService soundService) {
        this.tetrisController = tetrisController;
        this.fontCache = fontCache;
        this.imageCache = imageCache;
        this.soundService = soundService;
    }

    @Override
    public TetrisView createTetrisView(final Tetris tetris) {
        final TetrisInstanceView tetrisInstanceView = createTetrisInstanceView(tetris);

        return new TetrisViewImpl(tetrisController, tetrisInstanceView, fontCache, tetris, imageCache, soundService);
    }

    private TetrisInstanceView createTetrisInstanceView(final Tetris tetris) {
        final TetrisInstanceStatisticsView tetrisInstanceStatisticsView = createTetrisInstanceStatisticsView(tetris);
        final TetrisInstanceInfoView tetrisInstanceInfoView = createTetrisInstanceInfoView(tetris);

        return new TetrisInstanceViewImpl(fontCache, tetrisInstanceStatisticsView,
                tetrisInstanceInfoView, imageCache, tetris);
    }

    private TetrisInstanceStatisticsView createTetrisInstanceStatisticsView(final Tetris tetris) {
        return new TetrisInstanceStatisticsViewImpl(fontCache, imageCache, tetris);
    }

    private TetrisInstanceInfoView createTetrisInstanceInfoView(final Tetris tetris) {
        return new TetrisInstanceInfoViewImpl(fontCache, imageCache, tetris);
    }
}
