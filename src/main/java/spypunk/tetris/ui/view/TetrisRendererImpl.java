/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.factory.BlockImageFactory;
import spypunk.tetris.ui.factory.ContainerFactory;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisRendererImpl implements TetrisRenderer {

    private static final float DEFAULT_FONT_SIZE = 32F;

    private static final float GAME_OVER_FONT_SIZE = 42F;

    private static final Color GAME_OVER_FG_COLOR = new Color(30, 30, 30, 200);

    private static final String GAME_OVER = "GAME OVER";

    private static final Color DEFAULT_FONT_COLOR = Color.LIGHT_GRAY;

    private static final Color DEFAULT_CONTAINER_COLOR = Color.GRAY;

    @Inject
    private TetrisFrame tetrisFrame;

    @Inject
    private TetrisCanvas tetrisCanvas;

    @Inject
    private BlockImageFactory blockImageFactory;

    @Inject
    private ContainerFactory containerFactory;

    @Inject
    private ShapeFactory shapeFactory;

    private final Font defaultFont;

    private final Font gameOverFont;

    @Inject
    public TetrisRendererImpl(FontFactory fontFactory) {
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);
        gameOverFont = fontFactory.createDefaultFont(GAME_OVER_FONT_SIZE);
    }

    @Override
    public void start() {
        SwingUtils.doInAWTThread(() -> tetrisFrame.setVisible(true), true);
    }

    @Override
    public void render(Tetris tetris) {
        SwingUtils.doInAWTThread(() -> tetrisCanvas.render(graphics -> doRender(tetris, graphics)), true);
    }

    private void doRender(Tetris tetris, Graphics2D graphics) {
        renderBlocks(tetris, graphics);
        renderLevel(tetris, graphics);
        renderScore(tetris, graphics);
        renderRows(tetris, graphics);
        renderNextShape(tetris, graphics);
    }

    private void renderBlocks(Tetris tetris, Graphics2D graphics) {
        Container container = containerFactory.createTetrisContainer();

        renderContainer(graphics, container);

        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, BLOCK_SIZE + 1,
                    -BLOCK_SIZE + 1));

        if (tetris.isGameOver()) {
            renderGameOver(graphics, container);
        }
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        Container levelContainer = containerFactory.createLevelContainer();
        renderInfo(graphics, levelContainer, String.valueOf(tetris.getLevel()));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        Container scoreContainer = containerFactory.createScoreContainer();
        renderInfo(graphics, scoreContainer, String.valueOf(tetris.getScore()));
    }

    private void renderRows(Tetris tetris, Graphics2D graphics) {
        Container rowsContainer = containerFactory.createRowsContainer();
        renderInfo(graphics, rowsContainer, String.valueOf(tetris.getCompletedRows()));
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

        Image image = blockImageFactory.createBlockImage(shapeType);

        int dx1 = dx + block.getLocation().x * BLOCK_SIZE;
        int dx2 = dx1 + BLOCK_SIZE;
        int dy1 = dy + block.getLocation().y * BLOCK_SIZE;
        int dy2 = dy1 + BLOCK_SIZE;

        graphics.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, image.getWidth(null), image.getHeight(null), null);
    }

    private void renderInfo(Graphics2D graphics, Container container, String info) {
        renderContainer(graphics, container);
        renderTextCentered(graphics, info, container.getRectangle(), defaultFont);
    }

    public void renderContainer(Graphics2D graphics, Container container) {
        graphics.setColor(DEFAULT_CONTAINER_COLOR);

        Rectangle rectangle = container.getRectangle();

        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        String title = container.getTitle();

        if (title == null) {
            return;
        }

        renderTextCentered(graphics, title,
            new Rectangle(rectangle.x, rectangle.y - BLOCK_SIZE, rectangle.width, BLOCK_SIZE), defaultFont);
    }

    private void renderTextCentered(Graphics2D graphics, String text, Rectangle rectangle, Font font) {
        graphics.setColor(DEFAULT_FONT_COLOR);
        graphics.setFont(font);

        Point location = SwingUtils.getCenteredTextLocation(graphics, text, rectangle);
        graphics.drawString(text, location.x, location.y);
    }

    private void renderGameOver(Graphics2D graphics, Container container) {
        Rectangle rectangle = container.getRectangle();

        graphics.setColor(GAME_OVER_FG_COLOR);
        graphics.fillRect(rectangle.x + 1, rectangle.y + 1, rectangle.width - 1, rectangle.height - 1);

        renderTextCentered(graphics, GAME_OVER, rectangle, gameOverFont);
    }
}
