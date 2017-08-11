/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;

public abstract class AbstractTetrisView extends AbstractView {

    private final TetrisViewComponent tetrisViewComponent = new TetrisViewComponent();

    protected final class TetrisViewComponent extends JLabel {

        private static final long serialVersionUID = 7092310215094716291L;

        @Override
        protected void paintComponent(final Graphics graphics) {
            super.paintComponent(graphics);

            final Graphics2D graphics2d = (Graphics2D) graphics;

            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            doPaint(graphics2d);
        }
    }

    protected AbstractTetrisView(final FontCache fontCache, final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);
    }

    protected void initializeComponent(final int width, final int height) {
        tetrisViewComponent.setPreferredSize(new Dimension(width, height));
    }

    protected void initializeComponentWithBorders(final int width, final int height) {
        initializeComponent(width, height);

        tetrisViewComponent.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public Component getComponent() {
        return tetrisViewComponent;
    }

    protected abstract void doPaint(final Graphics2D graphics);
}
