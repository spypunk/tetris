/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.factory.BlockImageFactory;
import spypunk.tetris.ui.factory.ContainerFactory;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisViewImpl implements TetrisView {

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

    private static final String TITLE = "Tetris";

    private static final Dimension DEFAULT_DIMENSION = new Dimension(
            WIDTH * BLOCK_SIZE + 9 * BLOCK_SIZE,
            (HEIGHT - 2) * BLOCK_SIZE + 2 * BLOCK_SIZE);

    private static final float DEFAULT_FONT_SIZE = 32F;

    private static final float TETRIS_FROZEN_FONT_SIZE = 42F;

    private static final Color TETRIS_FROZEN_FG_COLOR = new Color(30, 30, 30, 150);

    private static final String GAME_OVER = "GAME OVER";

    private static final String PAUSE = "PAUSE";

    private static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;

    private static final Color DEFAULT_CONTAINER_COLOR = Color.GRAY;

    private final Font defaultFont;

    private final Font frozenTetrisFont;

    private final JFrame frame;

    private final JLabel label;

    private final BufferedImage image;

    @Inject
    private TetrisController tetrisController;

    @Inject
    private BlockImageFactory blockImageFactory;

    @Inject
    private ContainerFactory containerFactory;

    @Inject
    private ShapeFactory shapeFactory;

    @Inject
    public TetrisViewImpl(FontFactory fontFactory) {
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);
        frozenTetrisFont = fontFactory.createDefaultFont(TETRIS_FROZEN_FONT_SIZE);

        frame = new JFrame(TITLE);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new TetrisWindowListener());

        image = new BufferedImage(DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height, BufferedImage.TYPE_INT_ARGB);

        label = new JLabel(new ImageIcon(image));
        label.setFocusable(true);
        label.addKeyListener(new TetrisKeyAdapter());

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        SwingUtils.doInAWTThread(() -> frame.setVisible(visible), true);
    }

    @Override
    public void update(Tetris tetris) {
        SwingUtils.doInAWTThread(() -> doUpdate(tetris), true);
    }

    private void doUpdate(Tetris tetris) {
        Graphics2D graphics = initializeGraphics();

        renderBlocks(tetris, graphics);
        renderLevel(tetris, graphics);
        renderScore(tetris, graphics);
        renderRows(tetris, graphics);
        renderNextShape(tetris, graphics);

        graphics.dispose();

        label.repaint();
    }

    private Graphics2D initializeGraphics() {
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height);
        return graphics;
    }

    private void renderBlocks(Tetris tetris, Graphics2D graphics) {
        Container container = containerFactory.createTetrisContainer();

        renderContainer(graphics, container);

        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, BLOCK_SIZE + 1,
                    -BLOCK_SIZE + 1));

        State state = tetris.getState();

        if (!State.RUNNING.equals(state)) {
            renderTetrisFrozen(graphics, container, state);
        }
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        renderInfo(graphics, containerFactory.createLevelContainer(), String.valueOf(tetris.getLevel()));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        renderInfo(graphics, containerFactory.createScoreContainer(), String.valueOf(tetris.getScore()));
    }

    private void renderRows(Tetris tetris, Graphics2D graphics) {
        renderInfo(graphics, containerFactory.createRowsContainer(), String.valueOf(tetris.getCompletedRows()));
    }

    private void renderNextShape(Tetris tetris, Graphics2D graphics) {
        Container nextShapeContainer = containerFactory.createNextShapeContainer();

        renderContainer(graphics, nextShapeContainer);

        Shape nextShape = tetris.getNextShape();
        Shape previewShape = shapeFactory.createShape(nextShape.getShapeType(), nextShape.getCurrentRotation());
        Rectangle boundingBox = previewShape.getBoundingBox();
        Rectangle containerRectangle = nextShapeContainer.getRectangle();

        int width = containerRectangle.width;
        int height = containerRectangle.height;
        int dx = containerRectangle.x + (width - boundingBox.width * BLOCK_SIZE) / 2;
        int dy = containerRectangle.y + (height - boundingBox.height * BLOCK_SIZE) / 2;

        previewShape.getBlocks().stream().forEach(
            block -> renderBlock(graphics, block, dx, dy));
    }

    private void renderBlock(Graphics2D graphics, Block block, int dx, int dy) {
        ShapeType shapeType = block.getShape().getShapeType();

        Image blockImage = blockImageFactory.createBlockImage(shapeType);

        int dx1 = dx + block.getLocation().x * BLOCK_SIZE;
        int dx2 = dx1 + BLOCK_SIZE;
        int dy1 = dy + block.getLocation().y * BLOCK_SIZE;
        int dy2 = dy1 + BLOCK_SIZE;

        graphics.drawImage(blockImage, dx1, dy1, dx2, dy2, 0, 0, blockImage.getWidth(null), blockImage.getHeight(null),
            null);
    }

    private void renderInfo(Graphics2D graphics, Container container, String info) {
        renderContainer(graphics, container);
        renderTextCentered(graphics, info, container.getRectangle(), defaultFont);
    }

    private void renderContainer(Graphics2D graphics, Container container) {
        graphics.setColor(DEFAULT_CONTAINER_COLOR);

        Rectangle rectangle = container.getRectangle();

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        String title = container.getTitle();

        if (title != null) {
            renderTextCentered(graphics, title,
                new Rectangle(rectangle.x, rectangle.y - BLOCK_SIZE, rectangle.width, BLOCK_SIZE), defaultFont);
        }
    }

    private void renderTextCentered(Graphics2D graphics, String text, Rectangle rectangle, Font font) {
        graphics.setColor(DEFAULT_FONT_COLOR);
        graphics.setFont(font);

        Point location = SwingUtils.getCenteredTextLocation(graphics, text, rectangle);

        graphics.drawString(text, location.x, location.y);
    }

    private void renderTetrisFrozen(Graphics2D graphics, Container container, State state) {
        Rectangle rectangle = container.getRectangle();

        graphics.setColor(TETRIS_FROZEN_FG_COLOR);
        graphics.fillRect(rectangle.x + 1, rectangle.y + 1, rectangle.width - 1, rectangle.height - 1);

        renderTextCentered(graphics, State.GAME_OVER.equals(state) ? GAME_OVER : PAUSE, rectangle,
            frozenTetrisFont);
    }
}
