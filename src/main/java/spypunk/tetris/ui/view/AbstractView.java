/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;

public abstract class AbstractView {

    protected final FontCache fontCache;

    protected final ImageCache imageCache;

    protected final Tetris tetris;

    protected AbstractView(final FontCache fontCache, final ImageCache imageCache, final Tetris tetris) {
        this.fontCache = fontCache;
        this.imageCache = imageCache;
        this.tetris = tetris;
    }
}
