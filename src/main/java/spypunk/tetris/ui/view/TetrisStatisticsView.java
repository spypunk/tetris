/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.util.SwingUtils.Text;

public class TetrisStatisticsView extends AbstractTetrisView {

    private static final String STATISTICS = "STATISTICS";

    private final Rectangle statisticsRectangle;

    private final List<TetrisStatistic> tetrisStatistics;

    private final Text statisticsTitleText;

    private class TetrisStatistic {

        private final Image image;

        private final Rectangle imageRectangle;

        private final Rectangle textRectangle;

        private final ShapeType shapeType;

        TetrisStatistic(final Image image, final Rectangle imageRectangle,
                final Rectangle textRectangle,
                final ShapeType shapeType) {
            this.image = image;
            this.imageRectangle = imageRectangle;
            this.textRectangle = textRectangle;
            this.shapeType = shapeType;
        }

        public void render(final Graphics2D graphics) {
            final String value = String.valueOf(tetris.getStatistics().get(shapeType));

            SwingUtils.drawImage(graphics, image, imageRectangle);

            final Text statisticText = new Text(value, fontCache.getDefaultFont());

            SwingUtils.renderCenteredText(graphics, textRectangle, statisticText);
        }
    }

    public TetrisStatisticsView(final FontCache fontCache,
            final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        statisticsRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE * 15);

        tetrisStatistics = Arrays.asList(ShapeType.values())
                .stream()
                .map(shapeType -> createTetrisStatistic(imageCache, shapeType))
                .collect(Collectors.toList());

        statisticsTitleText = new Text(STATISTICS, fontCache.getDefaultFont());

        initializeComponent(statisticsRectangle.width + 1, statisticsRectangle.height + BLOCK_SIZE + 1);
    }

    private TetrisStatistic createTetrisStatistic(final ImageCache imageCache, final ShapeType shapeType) {
        final Image shapeImage = imageCache.getShapeImage(shapeType);

        final Rectangle imageContainerRectangle = new Rectangle(statisticsRectangle.x,
                statisticsRectangle.y + shapeType.ordinal() * 2 * BLOCK_SIZE + BLOCK_SIZE,
                statisticsRectangle.width / 2, BLOCK_SIZE);

        final Rectangle imageRectangle = SwingUtils.getCenteredImageRectangle(shapeImage, imageContainerRectangle, 0.5);

        final Rectangle textRectangle = new Rectangle(
                statisticsRectangle.x + imageContainerRectangle.width,
                imageContainerRectangle.y, imageContainerRectangle.width, imageContainerRectangle.height);

        return new TetrisStatistic(shapeImage, imageRectangle,
                textRectangle, shapeType);
    }

    @Override
    protected void doUpdate(final Graphics2D graphics) {
        SwingUtils.drawRectangleWithTitle(graphics, statisticsRectangle, statisticsTitleText);

        tetrisStatistics.forEach(tetrisStatistic -> tetrisStatistic.render(graphics));
    }
}
