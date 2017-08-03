/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_BORDER_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisInfoView extends AbstractTetrisView {

    private static final int VIEW_HEIGHT = 1 + BLOCK_SIZE * 16;

    private static final int VIEW_WIDTH = 1 + BLOCK_SIZE * 6;

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT";

    private static final String ROWS = "ROWS";

    private final Map<ShapeType, Rectangle> shapeTypeImageRectangles;

    private final TetrisInfo rowsTetrisInfo;

    private final TetrisInfo scoreTetrisInfo;

    private final TetrisInfo levelTetrisInfo;

    private final TetrisInfo nextShapeTetrisInfo;

    private static class TetrisInfo {

        public TetrisInfo(final Rectangle rectangle, final Rectangle titleRectangle, final String title) {
            this.rectangle = rectangle;
            this.titleRectangle = titleRectangle;
            this.title = title;
        }

        private final Rectangle rectangle;

        private final Rectangle titleRectangle;

        private final String title;

        private String value;

        public void setValue(final int value) {
            this.value = String.valueOf(value);
        }
    }

    public TetrisInfoView(final FontCache fontCache,
            final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        final Rectangle levelRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE);
        final Rectangle scoreRectangle = new Rectangle(0, BLOCK_SIZE * 4, BLOCK_SIZE * 6, BLOCK_SIZE);
        final Rectangle rowsRectangle = new Rectangle(0, BLOCK_SIZE * 7, BLOCK_SIZE * 6, BLOCK_SIZE);
        final Rectangle nextShapeRectangle = new Rectangle(0, BLOCK_SIZE * 10, BLOCK_SIZE * 6, BLOCK_SIZE * 6);

        final Rectangle levelTitleRectangle = createTitleRectangle(levelRectangle);
        final Rectangle scoreTitleRectangle = createTitleRectangle(scoreRectangle);
        final Rectangle rowsTitleRectangle = createTitleRectangle(rowsRectangle);
        final Rectangle nextShapeTitleRectangle = createTitleRectangle(nextShapeRectangle);

        rowsTetrisInfo = new TetrisInfo(rowsRectangle, rowsTitleRectangle, ROWS);
        scoreTetrisInfo = new TetrisInfo(scoreRectangle, scoreTitleRectangle, SCORE);
        levelTetrisInfo = new TetrisInfo(levelRectangle, levelTitleRectangle, LEVEL);
        nextShapeTetrisInfo = new TetrisInfo(nextShapeRectangle, nextShapeTitleRectangle, NEXT_SHAPE);

        shapeTypeImageRectangles = Arrays.asList(ShapeType.values())
                .stream()
                .collect(Collectors.toMap(Function.identity(), this::createShapeTypeImageRectangle));

        initializeComponent(VIEW_WIDTH, VIEW_HEIGHT);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        rowsTetrisInfo.setValue(tetris.getCompletedRows());
        scoreTetrisInfo.setValue(tetris.getScore());
        levelTetrisInfo.setValue(tetris.getLevel());

        renderLevel(graphics);
        renderScore(graphics);
        renderRows(graphics);
        renderNextShape(graphics);
    }

    private Rectangle createTitleRectangle(final Rectangle rectangle) {
        return new Rectangle(0, rectangle.y - BLOCK_SIZE, rectangle.width,
                BLOCK_SIZE);
    }

    private Rectangle createShapeTypeImageRectangle(final ShapeType shapeType) {
        final Image shapeTypeImage = imageCache.getShapeImage(shapeType);
        return SwingUtils.getCenteredImageRectangle(shapeTypeImage, nextShapeTetrisInfo.rectangle);
    }

    private void renderRows(final Graphics2D graphics) {
        renderInfo(graphics, rowsTetrisInfo);
    }

    private void renderScore(final Graphics2D graphics) {
        renderInfo(graphics, scoreTetrisInfo);
    }

    private void renderLevel(final Graphics2D graphics) {
        renderInfo(graphics, levelTetrisInfo);
    }

    private void renderNextShape(final Graphics2D graphics) {
        renderInfoWithoutValue(graphics, nextShapeTetrisInfo);

        final State tetrisState = tetris.getState();

        if (tetrisState.equals(State.STOPPED)) {
            return;
        }

        final Shape nextShape = tetris.getNextShape();
        final ShapeType shapeType = nextShape.getShapeType();
        final Image shapeTypeImage = imageCache.getShapeImage(shapeType);
        final Rectangle rectangle = shapeTypeImageRectangles.get(shapeType);

        SwingUtils.drawImage(graphics, shapeTypeImage, rectangle);
    }

    private void renderInfo(final Graphics2D graphics, final TetrisInfo tetrisInfo) {
        renderInfoWithoutValue(graphics, tetrisInfo);

        SwingUtils.renderCenteredText(graphics, tetrisInfo.value,
            tetrisInfo.rectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);
    }

    private void renderInfoWithoutValue(final Graphics2D graphics, final TetrisInfo tetrisInfo) {
        SwingUtils.renderCenteredText(graphics, tetrisInfo.title,
            tetrisInfo.titleRectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_BORDER_COLOR);

        final Rectangle rectangle = tetrisInfo.rectangle;

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width,
            rectangle.height);
    }
}
