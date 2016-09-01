/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_BORDER_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.model.TetrisInstance.State;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.FontType;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisInstanceGridViewImpl extends TetrisInstanceGridView {

    private static final long serialVersionUID = -3487901883637598196L;

    private static final String PAUSE = "PAUSE";

    private static final Color TETRIS_FROZEN_FG_COLOR = new Color(30, 30, 30, 200);

    private static final String GAME_OVER = "GAME OVER";

    private static final String PRESS_SPACE = "PRESS SPACE";

    private final BufferedImage image;

    private final Tetris tetris;

    private final ImageCache imageCache;

    private final Font frozenFont;

    private final Rectangle imageRectangle;

    public TetrisInstanceGridViewImpl(final FontCache fontCache,
            final ImageCache imageCache,
            final Tetris tetris) {
        this.imageCache = imageCache;
        this.tetris = tetris;

        frozenFont = fontCache.getFont(FontType.FROZEN);

        imageRectangle = new Rectangle(0, 0, TetrisConstants.WIDTH * BLOCK_SIZE,
                TetrisConstants.HEIGHT * BLOCK_SIZE);

        image = new BufferedImage(imageRectangle.width,
                imageRectangle.height,
                BufferedImage.TYPE_INT_ARGB);

        setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER_COLOR));
        setIcon(new ImageIcon(image));
        setIgnoreRepaint(true);
    }

    @Override
    public void update() {
        SwingUtils.doInGraphics(image, this::renderBlocks);
        repaint();
    }

    private void renderBlocks(final Graphics2D graphics) {
        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        final State state = tetrisInstance.getState();

        if (State.NEW.equals(state)) {
            renderTetrisNew(graphics);
            return;
        }

        tetrisInstance.getBlocks().values().stream()
                .forEach(block -> renderBlock(graphics, block));

        tetrisInstance.getCurrentShape().getBlocks().stream().forEach(block -> renderBlock(graphics, block));

        if (!State.RUNNING.equals(state)) {
            renderTetrisFrozen(graphics, state);
        }
    }

    private void renderBlock(final Graphics2D graphics, final Block block) {
        final ShapeType shapeType = block.getShape().getShapeType();
        final Image blockImage = imageCache.getBlockImage(shapeType);
        final Point location = block.getLocation();
        final int x1 = location.x * BLOCK_SIZE;
        final int y1 = location.y * BLOCK_SIZE;
        final Rectangle rectangle = new Rectangle(x1, y1, BLOCK_SIZE, BLOCK_SIZE);

        SwingUtils.drawImage(graphics, blockImage, rectangle);
    }

    private void renderTetrisNew(final Graphics2D graphics) {
        SwingUtils.renderCenteredText(graphics, PRESS_SPACE, imageRectangle,
            frozenFont, DEFAULT_FONT_COLOR);
    }

    private void renderTetrisFrozen(final Graphics2D graphics, final State state) {
        graphics.setColor(TETRIS_FROZEN_FG_COLOR);
        graphics.fillRect(imageRectangle.x, imageRectangle.y, imageRectangle.width,
            imageRectangle.height);

        SwingUtils.renderCenteredText(graphics, State.GAME_OVER.equals(state) ? GAME_OVER : PAUSE, imageRectangle,
            frozenFont, DEFAULT_FONT_COLOR);
    }
}
