/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_CONTAINER_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.GAME_OVER;
import static spypunk.tetris.ui.constants.TetrisUIConstants.INFO_CONTAINERS_WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.INFO_CONTAINERS_X;
import static spypunk.tetris.ui.constants.TetrisUIConstants.LEVEL;
import static spypunk.tetris.ui.constants.TetrisUIConstants.LEVEL_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.LEVEL_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.NEXT_SHAPE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.NEXT_SHAPE_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.NEXT_SHAPE_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.PAUSE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.ROWS;
import static spypunk.tetris.ui.constants.TetrisUIConstants.ROWS_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.ROWS_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.SCORE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.SCORE_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.SCORE_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.STATISTICS;
import static spypunk.tetris.ui.constants.TetrisUIConstants.STATISTICS_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.STATISTICS_CONTAINER_WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.STATISTICS_CONTAINER_X;
import static spypunk.tetris.ui.constants.TetrisUIConstants.STATISTICS_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_CONTAINER_HEIGHT;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_CONTAINER_WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_CONTAINER_X;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_CONTAINER_Y;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_FROZEN_FG_COLOR;
import static spypunk.tetris.ui.constants.TetrisUIConstants.TETRIS_FROZEN_FONT_SIZE;
import static spypunk.tetris.ui.constants.TetrisUIConstants.VIEW_DIMENSION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.model.TetrisInstance.State;
import spypunk.tetris.ui.constants.TetrisUIConstants;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.factory.ImageFactory;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisViewImpl implements TetrisView {

    private final class TetrisURLLabelMouseAdapter extends MouseAdapter {
        private final JLabel urlLabel;

        private TetrisURLLabelMouseAdapter(JLabel urlLabel) {
            this.urlLabel = urlLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            tetrisController.onURLClicked();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            urlLabel.setForeground(Color.CYAN);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            urlLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }

    private final class TetrisWindowListener extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private final class TetrisKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            tetrisController.onKeyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            tetrisController.onKeyReleased(e.getKeyCode());
        }
    }

    private final Tetris tetris;

    private final FontFactory fontFactory;

    private Font defaultFont;

    private Font frozenTetrisFont;

    private Font urlFont;

    private JFrame frame;

    private JLabel imageLabel;

    private BufferedImage image;

    private Container statisticsContainer;

    private Container tetrisContainer;

    private Container nextShapeContainer;

    private Container levelContainer;

    private Container scoreContainer;

    private Container rowsContainer;

    @Inject
    private TetrisController tetrisController;

    @Inject
    private ImageFactory imageFactory;

    @Inject
    public TetrisViewImpl(FontFactory fontFactory, TetrisController tetrisController) {
        this.fontFactory = fontFactory;
        this.tetrisController = tetrisController;
        this.tetris = tetrisController.getTetris();

        initializeFonts();
        initializeContainers();
        initializeFrame();
    }

    @Override
    public void show() {
        SwingUtils.doInAWTThread(() -> frame.setVisible(true), true);
    }

    @Override
    public void update() {
        SwingUtils.doInAWTThread(() -> doUpdate(), true);
    }

    private void initializeFonts() {
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);
        frozenTetrisFont = fontFactory.createDefaultFont(TETRIS_FROZEN_FONT_SIZE);
        urlFont = fontFactory.createURLFont(10.0f);
    }

    private void initializeContainers() {
        tetrisContainer = initializeContainer(TETRIS_CONTAINER_X, TETRIS_CONTAINER_Y,
            TETRIS_CONTAINER_WIDTH,
            TETRIS_CONTAINER_HEIGHT);
        nextShapeContainer = initializeContainer(INFO_CONTAINERS_X, NEXT_SHAPE_CONTAINER_Y, INFO_CONTAINERS_WIDTH,
            NEXT_SHAPE_CONTAINER_HEIGHT,
            NEXT_SHAPE);
        levelContainer = initializeContainer(INFO_CONTAINERS_X, LEVEL_CONTAINER_Y, INFO_CONTAINERS_WIDTH,
            LEVEL_CONTAINER_HEIGHT,
            LEVEL);
        scoreContainer = initializeContainer(INFO_CONTAINERS_X, SCORE_CONTAINER_Y, INFO_CONTAINERS_WIDTH,
            SCORE_CONTAINER_HEIGHT,
            SCORE);
        rowsContainer = initializeContainer(INFO_CONTAINERS_X, ROWS_CONTAINER_Y, INFO_CONTAINERS_WIDTH,
            ROWS_CONTAINER_HEIGHT,
            ROWS);
        statisticsContainer = initializeContainer(STATISTICS_CONTAINER_X, STATISTICS_CONTAINER_Y,
            STATISTICS_CONTAINER_WIDTH,
            STATISTICS_CONTAINER_HEIGHT,
            STATISTICS);
    }

    private void initializeFrame() {
        frame = new JFrame(tetris.getName() + " " + tetris.getVersion());

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new TetrisWindowListener());

        image = new BufferedImage(VIEW_DIMENSION.width, VIEW_DIMENSION.height, BufferedImage.TYPE_INT_ARGB);

        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setFocusable(true);
        imageLabel.addKeyListener(new TetrisKeyAdapter());

        final JPanel urlPanel = initializeURLPanel();

        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(urlPanel, BorderLayout.SOUTH);
        frame.pack();

        frame.setLocationRelativeTo(null);
    }

    private JPanel initializeURLPanel() {
        URI projectURI = tetris.getProjectURI();

        final JLabel urlLabel = new JLabel(projectURI.getHost() + projectURI.getPath());

        urlLabel.setFont(urlFont);
        urlLabel.setForeground(DEFAULT_FONT_COLOR);
        urlLabel.addMouseListener(new TetrisURLLabelMouseAdapter(urlLabel));

        final JPanel urlPanel = new JPanel(new BorderLayout());

        urlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        urlPanel.setBackground(Color.BLACK);
        urlPanel.setOpaque(true);
        urlPanel.add(urlLabel, BorderLayout.EAST);

        return urlPanel;
    }

    private void doUpdate() {
        final Graphics2D graphics = initializeGraphics();

        renderBlocks(graphics);
        renderLevel(graphics);
        renderScore(graphics);
        renderRows(graphics);
        renderNextShape(graphics);
        renderStatistics(graphics);

        graphics.dispose();

        imageLabel.repaint();
    }

    private Graphics2D initializeGraphics() {
        final Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, VIEW_DIMENSION.width, VIEW_DIMENSION.height);

        return graphics;
    }

    private void renderBlocks(Graphics2D graphics) {
        renderContainer(graphics, tetrisContainer);

        final Rectangle rectangle = tetrisContainer.getRectangle();

        TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        tetrisInstance.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, rectangle.x + 1,
                    rectangle.y - TetrisUIConstants.TETRIS_CONTAINER_Y - BLOCK_SIZE + 1));

        final State state = tetrisInstance.getState();

        if (!State.RUNNING.equals(state)) {
            renderTetrisFrozen(graphics, tetrisContainer, state);
        }
    }

    private void renderLevel(Graphics2D graphics) {
        renderTextInContainer(graphics, levelContainer, String.valueOf(tetris.getTetrisInstance().getLevel()));
    }

    private void renderScore(Graphics2D graphics) {
        renderTextInContainer(graphics, scoreContainer, String.valueOf(tetris.getTetrisInstance().getScore()));
    }

    private void renderRows(Graphics2D graphics) {
        renderTextInContainer(graphics, rowsContainer, String.valueOf(tetris.getTetrisInstance().getCompletedRows()));
    }

    private void renderNextShape(Graphics2D graphics) {
        final Shape nextShape = tetris.getTetrisInstance().getNextShape();

        renderContainer(graphics, nextShapeContainer);

        final Image shapeImage = imageFactory.createShapeImage(nextShape.getShapeType());
        final Rectangle rectangle = SwingUtils.getCenteredImageRectangle(shapeImage, nextShapeContainer.getRectangle());

        SwingUtils.drawImage(graphics, shapeImage, rectangle);
    }

    private void renderStatistics(Graphics2D graphics) {
        renderContainer(graphics, statisticsContainer);

        tetris.getTetrisInstance().getStatistics().entrySet()
                .forEach(statisticEntry -> renderStatistic(graphics, statisticEntry.getKey(),
                    String.valueOf(statisticEntry.getValue())));
    }

    private void renderBlock(Graphics2D graphics, Block block, int dx, int dy) {
        final ShapeType shapeType = block.getShape().getShapeType();

        final Image blockImage = imageFactory.createBlockImage(shapeType);

        final int x1 = dx + block.getLocation().x * BLOCK_SIZE;
        final int x2 = dy + block.getLocation().y * BLOCK_SIZE;

        final Rectangle rectangle = new Rectangle(x1, x2, BLOCK_SIZE, BLOCK_SIZE);

        SwingUtils.drawImage(graphics, blockImage, rectangle);
    }

    private void renderTextInContainer(Graphics2D graphics, Container container, String info) {
        renderContainer(graphics, container);
        renderText(graphics, info, container.getRectangle());
    }

    private void renderContainer(Graphics2D graphics, Container container) {
        graphics.setColor(DEFAULT_CONTAINER_COLOR);

        final Rectangle rectangle = container.getRectangle();

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        final String title = container.getTitle();

        if (title != null) {
            renderText(graphics, title,
                new Rectangle(rectangle.x, rectangle.y - BLOCK_SIZE, rectangle.width, BLOCK_SIZE), defaultFont);
        }
    }

    private void renderStatistic(Graphics2D graphics, ShapeType shapeType, String value) {
        final Image shapeImage = imageFactory.createShapeImage(shapeType);
        final Rectangle containerRectangle = statisticsContainer.getRectangle();

        final Rectangle imageContainerRectangle = new Rectangle(containerRectangle.x,
                containerRectangle.y + shapeType.ordinal() * 2 * BLOCK_SIZE + BLOCK_SIZE,
                containerRectangle.width / 2, BLOCK_SIZE);

        final Rectangle imageRectangle = SwingUtils.getCenteredImageRectangle(shapeImage, imageContainerRectangle,
            0.5);

        SwingUtils.drawImage(graphics, shapeImage, imageRectangle);

        final Rectangle textContainerRectangle = new Rectangle(containerRectangle.x + imageContainerRectangle.width,
                imageContainerRectangle.y, imageContainerRectangle.width, imageContainerRectangle.height);

        final Rectangle textRectangle = SwingUtils.getCenteredTextRectangle(graphics, value, textContainerRectangle,
            defaultFont);

        graphics.drawString(value, textRectangle.x, textRectangle.y);
    }

    private void renderText(Graphics2D graphics, String text, Rectangle rectangle) {
        renderText(graphics, text, rectangle, defaultFont);
    }

    private void renderText(Graphics2D graphics, String text, Rectangle rectangle, Font font) {
        renderText(graphics, text, rectangle, font, DEFAULT_FONT_COLOR);
    }

    private void renderText(Graphics2D graphics, String text, Rectangle rectangle, Font font, Color fontColor) {
        graphics.setFont(font);
        graphics.setColor(fontColor);

        final Rectangle textRectangle = SwingUtils.getCenteredTextRectangle(graphics, text, rectangle, font);

        graphics.drawString(text, textRectangle.x, textRectangle.y);
    }

    private void renderTetrisFrozen(Graphics2D graphics, Container container, State state) {
        final Rectangle rectangle = container.getRectangle();

        graphics.setColor(TETRIS_FROZEN_FG_COLOR);
        graphics.fillRect(rectangle.x + 1, rectangle.y + 1, rectangle.width - 1, rectangle.height - 1);

        renderText(graphics, State.GAME_OVER.equals(state) ? GAME_OVER : PAUSE, rectangle,
            frozenTetrisFont);
    }

    private Container initializeContainer(int x, int y, int width, int height, String title) {
        return Container.Builder.instance()
                .setRectangle(new Rectangle(x, y, width, height))
                .setTitle(title)
                .build();
    }

    private Container initializeContainer(int x, int y, int width, int height) {
        return initializeContainer(x, y, width, height, null);
    }
}
