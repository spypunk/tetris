/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.constants.TetrisUIConstants;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public abstract class AbstractTetrisView extends AbstractView {

    protected JLabel component;

    protected AbstractTetrisView(final FontCache fontCache, final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);
    }

    protected void initializeComponent(final int width, final int height, final boolean withBorders) {
        component = new JLabel();

        final BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        component.setIcon(new ImageIcon(image));
        component.setIgnoreRepaint(true);

        if (withBorders) {
            component.setBorder(BorderFactory.createLineBorder(TetrisUIConstants.DEFAULT_BORDER_COLOR));
        }
    }

    protected void initializeComponent(final int width, final int height) {
        initializeComponent(width, height, false);
    }

    @Override
    public void update() {
        final BufferedImage image = (BufferedImage) ((ImageIcon) component.getIcon()).getImage();

        SwingUtils.doInGraphics(image, this::doUpdate);

        component.repaint();
    }

    public Component getComponent() {
        return component;
    }

    protected abstract void doUpdate(final Graphics2D graphics);
}
