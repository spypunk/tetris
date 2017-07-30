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
import static spypunk.tetris.ui.constants.TetrisUIConstants.ZERO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisInstanceInfoView extends AbstractTetrisInstanceView {

    private static final int VIEW_HEIGHT = 1 + BLOCK_SIZE * 16;

    private static final int VIEW_WIDTH = 1 + BLOCK_SIZE * 6;

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT";

    private static final String ROWS = "ROWS";

    private final Rectangle levelRectangle;

    private final Rectangle scoreRectangle;

    private final Rectangle rowsRectangle;

    private final Rectangle nextShapeRectangle;

    private final Rectangle levelLabelRectangle;

    private final Rectangle scoreLabelRectangle;

    private final Rectangle rowsLabelRectangle;

    private final Rectangle nextShapeLabelRectangle;

    private final Map<ShapeType, Rectangle> shapeTypeImageRectangles;

    @Inject
    public TetrisInstanceInfoView(final FontCache fontCache,
            final ImageCache imageCache, final @TetrisProvider Tetris tetris) {
        super(fontCache, imageCache, tetris);

        levelRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE);
        scoreRectangle = new Rectangle(0, BLOCK_SIZE * 4, BLOCK_SIZE * 6, BLOCK_SIZE);
        rowsRectangle = new Rectangle(0, BLOCK_SIZE * 7, BLOCK_SIZE * 6, BLOCK_SIZE);
        nextShapeRectangle = new Rectangle(0, BLOCK_SIZE * 10, BLOCK_SIZE * 6, BLOCK_SIZE * 6);

        levelLabelRectangle = createLabelRectangle(levelRectangle);
        scoreLabelRectangle = createLabelRectangle(scoreRectangle);
        rowsLabelRectangle = createLabelRectangle(rowsRectangle);
        nextShapeLabelRectangle = createLabelRectangle(nextShapeRectangle);

        final List<ShapeType> shapeTypes = Lists.newArrayList(ShapeType.values());

        shapeTypeImageRectangles = shapeTypes.stream()
                .collect(
                    Collectors.toMap(Function.identity(), this::createShapeTypeImageRectangle));

        initializeComponent(VIEW_WIDTH, VIEW_HEIGHT);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        renderLevel(graphics);
        renderScore(graphics);
        renderRows(graphics);
        renderNextShape(graphics);
    }

    private Rectangle createLabelRectangle(final Rectangle rectangle) {
        return new Rectangle(0, rectangle.y - BLOCK_SIZE, rectangle.width,
                BLOCK_SIZE);
    }

    private Rectangle createShapeTypeImageRectangle(final ShapeType shapeType) {
        final Image shapeTypeImage = imageCache.getShapeImage(shapeType);
        return SwingUtils.getCenteredImageRectangle(shapeTypeImage, nextShapeRectangle);
    }

    private void renderRows(final Graphics2D graphics) {
        renderInfo(graphics, rowsRectangle, rowsLabelRectangle, ROWS,
            !tetris.getState().equals(State.STOPPED) ? String.valueOf(tetris.getTetrisInstance().getCompletedRows())
                    : ZERO);
    }

    private void renderScore(final Graphics2D graphics) {
        renderInfo(graphics, scoreRectangle, scoreLabelRectangle, SCORE,
            !tetris.getState().equals(State.STOPPED) ? String.valueOf(tetris.getTetrisInstance().getScore()) : ZERO);
    }

    private void renderLevel(final Graphics2D graphics) {
        renderInfo(graphics, levelRectangle, levelLabelRectangle, LEVEL,
            !tetris.getState().equals(State.STOPPED) ? String.valueOf(tetris.getTetrisInstance().getLevel()) : ZERO);
    }

    private void renderNextShape(final Graphics2D graphics) {
        renderLabelAndRectangle(graphics, nextShapeRectangle, nextShapeLabelRectangle, NEXT_SHAPE);

        final State tetrisState = tetris.getState();

        if (tetrisState.equals(State.STOPPED)) {
            return;
        }

        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        final Shape nextShape = tetrisInstance.getNextShape();
        final ShapeType shapeType = nextShape.getShapeType();
        final Image shapeTypeImage = imageCache.getShapeImage(shapeType);
        final Rectangle rectangle = shapeTypeImageRectangles.get(shapeType);

        SwingUtils.drawImage(graphics, shapeTypeImage, rectangle);
    }

    private void renderInfo(final Graphics2D graphics, final Rectangle rectangle, final Rectangle labelRectangle,
            final String title, final String value) {
        renderLabelAndRectangle(graphics, rectangle, labelRectangle, title);

        SwingUtils.renderCenteredText(graphics, value,
            rectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);
    }

    private void renderLabelAndRectangle(final Graphics2D graphics, final Rectangle rectangle,
            final Rectangle labelRectangle,
            final String label) {
        SwingUtils.renderCenteredText(graphics, label,
            labelRectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_BORDER_COLOR);

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width,
            rectangle.height);
    }
}
