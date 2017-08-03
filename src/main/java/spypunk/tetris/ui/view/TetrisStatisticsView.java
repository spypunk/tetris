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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisStatisticsView extends AbstractTetrisView {

    private static final String STATISTICS = "STATISTICS";

    private final Rectangle statisticsRectangle;

    private final Rectangle statisticsLabelRectangle;

    private final Map<ShapeType, StatisticsRow> statisticsRows;

    private final List<ShapeType> shapeTypes;

    private static class StatisticsRow {

        private final Image image;

        private final Rectangle imageRectangle;

        private final Rectangle textContainerRectangle;

        StatisticsRow(final Image image, final Rectangle imageRectangle,
                final Rectangle textContainerRectangle) {
            this.image = image;
            this.imageRectangle = imageRectangle;
            this.textContainerRectangle = textContainerRectangle;
        }

        public Image getImage() {
            return image;
        }

        public Rectangle getImageRectangle() {
            return imageRectangle;
        }

        public Rectangle getTextContainerRectangle() {
            return textContainerRectangle;
        }
    }

    public TetrisStatisticsView(final FontCache fontCache,
            final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        shapeTypes = Arrays.asList(ShapeType.values());

        statisticsRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE * 15);
        statisticsLabelRectangle = new Rectangle(0, 0, statisticsRectangle.width, BLOCK_SIZE);

        statisticsRows = shapeTypes
                .stream()
                .collect(Collectors.toMap(Function.identity(), shapeType -> createStatisticRow(imageCache, shapeType)));

        initializeComponent(statisticsRectangle.width + 1, statisticsRectangle.height + BLOCK_SIZE + 1);
    }

    private StatisticsRow createStatisticRow(final ImageCache imageCache, final ShapeType shapeType) {
        final Image shapeImage = imageCache.getShapeImage(shapeType);
        final Rectangle imageContainerRectangle = new Rectangle(statisticsRectangle.x,
                statisticsRectangle.y + shapeType.ordinal() * 2 * BLOCK_SIZE + BLOCK_SIZE,
                statisticsRectangle.width / 2, BLOCK_SIZE);

        final Rectangle imageRectangle = SwingUtils.getCenteredImageRectangle(shapeImage,
            imageContainerRectangle,
            0.5);

        final Rectangle textContainerRectangle = new Rectangle(
                statisticsRectangle.x + imageContainerRectangle.width,
                imageContainerRectangle.y, imageContainerRectangle.width, imageContainerRectangle.height);

        return new StatisticsRow(shapeImage, imageRectangle,
                textContainerRectangle);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        SwingUtils.renderCenteredText(graphics, STATISTICS,
            statisticsLabelRectangle, fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_BORDER_COLOR);

        graphics.drawRect(statisticsRectangle.x, statisticsRectangle.y, statisticsRectangle.width,
            statisticsRectangle.height);

        shapeTypes.forEach(shapeType -> renderStatistic(graphics, shapeType));
    }

    private void renderStatistic(final Graphics2D graphics, final ShapeType shapeType) {
        final StatisticsRow statisticsRow = statisticsRows.get(shapeType);
        final String value = String.valueOf(tetris.getStatistics().get(shapeType));

        SwingUtils.drawImage(graphics, statisticsRow.getImage(), statisticsRow.getImageRectangle());

        SwingUtils.renderCenteredText(graphics, value,
            statisticsRow.getTextContainerRectangle(), fontCache.getDefaultFont(), DEFAULT_FONT_COLOR);
    }
}
