package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_CONTAINER_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_SIZE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.factory.ImageFactory;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisInstanceStatisticsViewImpl implements TetrisInstanceStatisticsView {

    private static class StatisticsRow {

        private final Image image;

        private final Rectangle imageRectangle;

        private final Rectangle textContainerRectangle;

        public StatisticsRow(Image image, Rectangle imageRectangle,
                Rectangle textContainerRectangle) {
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

    private static final String STATISTICS = "STATISTICS";

    private final JPanel panel;

    private final BufferedImage image;

    private final Font defaultFont;

    private final Tetris tetris;

    private final Rectangle statisticsRectangle;

    private final Rectangle statisticsLabelRectangle;

    private final Map<ShapeType, StatisticsRow> statisticsRows;

    @Inject
    public TetrisInstanceStatisticsViewImpl(FontFactory fontFactory, TetrisController tetrisController,
            ImageFactory imageFactory) {
        tetris = tetrisController.getTetris();
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);

        statisticsRectangle = new Rectangle(0, BLOCK_SIZE, BLOCK_SIZE * 6, BLOCK_SIZE * 15);
        statisticsLabelRectangle = new Rectangle(0, 0, statisticsRectangle.width, BLOCK_SIZE);

        statisticsRows = Lists.newArrayList(ShapeType.values()).stream()
                .collect(
                    Collectors.toMap(Function.identity(), shapeType -> createStatisticRow(imageFactory, shapeType)));

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.setOpaque(true);

        image = new BufferedImage(statisticsRectangle.width + 1, statisticsRectangle.height + BLOCK_SIZE + 1,
                BufferedImage.TYPE_INT_ARGB);

        final JLabel label = new JLabel(new ImageIcon(image));

        panel.add(label);
    }

    @Override
    public void update() {
        final Graphics2D graphics = SwingUtils.createGraphics(image);

        renderStatistics(graphics);

        graphics.dispose();
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    private StatisticsRow createStatisticRow(ImageFactory imageFactory, ShapeType shapeType) {
        final Image shapeImage = imageFactory.createShapeImage(shapeType);
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

    private void renderStatistics(Graphics2D graphics) {
        SwingUtils.renderCenteredText(graphics, STATISTICS,
            statisticsLabelRectangle, defaultFont, DEFAULT_FONT_COLOR);

        graphics.setColor(DEFAULT_CONTAINER_COLOR);

        graphics.drawRect(statisticsRectangle.x, statisticsRectangle.y, statisticsRectangle.width,
            statisticsRectangle.height);

        Lists.newArrayList(ShapeType.values()).forEach(shapeType -> renderStatistic(graphics, shapeType));
    }

    private void renderStatistic(Graphics2D graphics, ShapeType shapeType) {
        final StatisticsRow statisticsRow = statisticsRows.get(shapeType);

        SwingUtils.drawImage(graphics, statisticsRow.getImage(), statisticsRow.getImageRectangle());

        final Map<ShapeType, Integer> statistics = tetris.getTetrisInstance().getStatistics();

        final String value = String.valueOf(statistics.get(shapeType));

        SwingUtils.renderCenteredText(graphics, value,
            statisticsRow.getTextContainerRectangle(), defaultFont, DEFAULT_FONT_COLOR);
    }
}
