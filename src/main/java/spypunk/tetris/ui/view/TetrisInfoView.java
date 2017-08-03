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

import org.apache.commons.lang3.tuple.Pair;

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

    private final ValueTetrisInfo rowsTetrisInfo;

    private final ValueTetrisInfo scoreTetrisInfo;

    private final ValueTetrisInfo levelTetrisInfo;

    private final NextShapeTetrisInfo nextShapeTetrisInfo;

    private abstract class TetrisInfo {

        protected final Rectangle rectangle;

        protected final Rectangle titleRectangle;

        protected final String title;

        public TetrisInfo(final Rectangle rectangle, final String title) {
            this.rectangle = rectangle;
            this.title = title;
            titleRectangle = createTitleRectangle(rectangle);
        }

        public void render(final Graphics2D graphics) {
            SwingUtils.renderCenteredText(graphics, title,
                titleRectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);

            graphics.setColor(DEFAULT_BORDER_COLOR);
            graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }

        private Rectangle createTitleRectangle(final Rectangle rectangle) {
            return new Rectangle(0, rectangle.y - BLOCK_SIZE, rectangle.width,
                    BLOCK_SIZE);
        }
    }

    private class ValueTetrisInfo extends TetrisInfo {

        public ValueTetrisInfo(final Rectangle rectangle, final String title) {
            super(rectangle, title);
        }

        public void render(final Graphics2D graphics, final int value) {
            super.render(graphics);

            SwingUtils.renderCenteredText(graphics, String.valueOf(value),
                rectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);
        }
    }

    private class NextShapeTetrisInfo extends TetrisInfo {

        private final Map<ShapeType, Pair<Image, Rectangle>> shapeTypeImageRectangles;

        public NextShapeTetrisInfo() {
            super(new Rectangle(0, BLOCK_SIZE * 10, BLOCK_SIZE * 6, BLOCK_SIZE * 6), NEXT_SHAPE);

            shapeTypeImageRectangles = Arrays.asList(ShapeType.values())
                    .stream()
                    .collect(Collectors.toMap(Function.identity(), this::createShapeTypeImageRectangle));
        }

        @Override
        public void render(final Graphics2D graphics) {
            super.render(graphics);

            final State tetrisState = tetris.getState();

            if (tetrisState.equals(State.STOPPED)) {
                return;
            }

            final ShapeType shapeType = tetris.getNextShape().getShapeType();
            final Pair<Image, Rectangle> shapeTypeImageRectangle = shapeTypeImageRectangles.get(shapeType);

            SwingUtils.drawImage(graphics, shapeTypeImageRectangle.getLeft(), shapeTypeImageRectangle.getRight());
        }

        private Pair<Image, Rectangle> createShapeTypeImageRectangle(final ShapeType shapeType) {
            final Image shapeTypeImage = imageCache.getShapeImage(shapeType);

            return Pair.of(shapeTypeImage, SwingUtils.getCenteredImageRectangle(shapeTypeImage, rectangle));
        }
    }

    public TetrisInfoView(final FontCache fontCache,
            final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        final Rectangle levelRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE);
        final Rectangle scoreRectangle = new Rectangle(0, BLOCK_SIZE * 4, BLOCK_SIZE * 6, BLOCK_SIZE);
        final Rectangle rowsRectangle = new Rectangle(0, BLOCK_SIZE * 7, BLOCK_SIZE * 6, BLOCK_SIZE);

        rowsTetrisInfo = new ValueTetrisInfo(rowsRectangle, ROWS);
        scoreTetrisInfo = new ValueTetrisInfo(scoreRectangle, SCORE);
        levelTetrisInfo = new ValueTetrisInfo(levelRectangle, LEVEL);
        nextShapeTetrisInfo = new NextShapeTetrisInfo();

        initializeComponent(VIEW_WIDTH, VIEW_HEIGHT);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        levelTetrisInfo.render(graphics, tetris.getLevel());
        scoreTetrisInfo.render(graphics, tetris.getScore());
        rowsTetrisInfo.render(graphics, tetris.getCompletedRows());
        nextShapeTetrisInfo.render(graphics);
    }
}
