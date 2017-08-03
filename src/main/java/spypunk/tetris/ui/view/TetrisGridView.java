/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.model.Shape.Block;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisGridView extends AbstractTetrisView {

    private static final String PAUSE = "PAUSE";

    private static final Color NOT_RUNNING_FG_COLOR = new Color(30, 30, 30, 200);

    private static final String GAME_OVER = "GAME OVER";

    private static final String PRESS_SPACE = "PRESS SPACE";

    private final Rectangle gridRectangle;

    public TetrisGridView(final FontCache fontCache,
            final ImageCache imageCache,
            final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        gridRectangle = new Rectangle(0, 0, TetrisConstants.WIDTH * BLOCK_SIZE,
                TetrisConstants.HEIGHT * BLOCK_SIZE);

        initializeComponent(gridRectangle.width, gridRectangle.height, true);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        final State tetrisState = tetris.getState();

        if (tetrisState.equals(State.STOPPED)) {
            renderTetrisStopped(graphics);
            return;
        }

        tetris.getBlocks().values().forEach(block -> renderBlock(graphics, block));
        tetris.getCurrentShape().getBlocks().forEach(block -> renderBlock(graphics, block));

        if (!State.RUNNING.equals(tetrisState)) {
            renderTetrisNotRunning(graphics, tetrisState);
        }
    }

    private void renderBlock(final Graphics2D graphics, final Block block) {
        final ShapeType shapeType = block.getShape().getShapeType();
        final Image blockImage = imageCache.getBlockImage(shapeType);
        final Point location = block.getLocation();
        final Rectangle rectangle = new Rectangle(location.x * BLOCK_SIZE, location.y * BLOCK_SIZE, BLOCK_SIZE,
                BLOCK_SIZE);

        SwingUtils.drawImage(graphics, blockImage, rectangle);
    }

    private void renderTetrisStopped(final Graphics2D graphics) {
        SwingUtils.renderCenteredText(graphics, PRESS_SPACE, gridRectangle,
            fontCache.getFrozenFont(), DEFAULT_FONT_COLOR);
    }

    private void renderTetrisNotRunning(final Graphics2D graphics, final State state) {
        graphics.setColor(NOT_RUNNING_FG_COLOR);
        graphics.fillRect(gridRectangle.x, gridRectangle.y, gridRectangle.width,
            gridRectangle.height);

        SwingUtils.renderCenteredText(graphics, State.GAME_OVER.equals(state) ? GAME_OVER : PAUSE, gridRectangle,
            fontCache.getFrozenFont(), DEFAULT_FONT_COLOR);
    }
}
