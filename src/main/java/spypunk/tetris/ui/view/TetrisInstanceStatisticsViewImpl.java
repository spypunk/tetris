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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import com.google.common.collect.Lists;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.FontType;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisInstanceStatisticsViewImpl extends TetrisInstanceStatisticsView {

    private static class StatisticsRow {

        private final Image image;

        private final Rectangle imageRectangle;

        private final Rectangle textContainerRectangle;

        public StatisticsRow(final Image image, final Rectangle imageRectangle,
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

    private static final long serialVersionUID = 288335810615538818L;

    private static final String STATISTICS = "STATISTICS";

    private final BufferedImage image;

    private final Tetris tetris;

    private final Rectangle statisticsRectangle;

    private final Rectangle statisticsLabelRectangle;

    private final Map<ShapeType, StatisticsRow> statisticsRows;

    private final List<ShapeType> shapeTypes;

    private final Font defaultFont;

    public TetrisInstanceStatisticsViewImpl(final FontCache fontCache,
            final ImageCache imageCache, final Tetris tetris) {
        this.tetris = tetris;

        defaultFont = fontCache.getFont(FontType.DEFAULT);

        shapeTypes = Lists.newArrayList(ShapeType.values());

        statisticsRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE * 15);
        statisticsLabelRectangle = new Rectangle(0, 0, statisticsRectangle.width, BLOCK_SIZE);

        statisticsRows = shapeTypes.stream()
                .collect(
                    Collectors.toMap(Function.identity(), shapeType -> createStatisticRow(imageCache, shapeType)));

        image = new BufferedImage(statisticsRectangle.width + 1, statisticsRectangle.height + BLOCK_SIZE + 1,
                BufferedImage.TYPE_INT_ARGB);

        setIcon(new ImageIcon(image));

        setLayout(new GridBagLayout());
        setIgnoreRepaint(true);
    }

    @Override
    public void update() {
        SwingUtils.doInGraphics(image, this::renderStatistics);
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

    private void renderStatistics(final Graphics2D graphics) {
        SwingUtils.renderCenteredText(graphics, STATISTICS,
            statisticsLabelRectangle, defaultFont, DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_BORDER_COLOR);

        graphics.drawRect(statisticsRectangle.x, statisticsRectangle.y, statisticsRectangle.width,
            statisticsRectangle.height);

        shapeTypes.forEach(shapeType -> renderStatistic(graphics, shapeType));

        repaint();
    }

    private void renderStatistic(final Graphics2D graphics, final ShapeType shapeType) {
        final StatisticsRow statisticsRow = statisticsRows.get(shapeType);
        final Map<ShapeType, Integer> statistics = tetris.getTetrisInstance().getStatistics();
        final String value = String.valueOf(statistics.get(shapeType));

        SwingUtils.drawImage(graphics, statisticsRow.getImage(), statisticsRow.getImageRectangle());

        SwingUtils.renderCenteredText(graphics, value,
            statisticsRow.getTextContainerRectangle(), defaultFont, DEFAULT_FONT_COLOR);
    }
}
