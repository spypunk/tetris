package spypunk.tetris.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.factory.BlockImageFactory;
import spypunk.tetris.factory.FontFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.util.SwingUtils;
import spypunk.tetris.view.Container.Builder;

@Singleton
public class TetrisRendererImpl implements TetrisRenderer {

    private static final float DEFAULT_FONT_SIZE = 32.0f;

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT SHAPE";

    private static final String ROWS = "ROWS";

    @Inject
    private TetrisFrame tetrisFrame;

    @Inject
    private TetrisCanvas tetrisCanvas;

    @Inject
    private BlockImageFactory blockImageFactory;

    private final Container tetrisContainer;

    private final Container nextShapeContainer;

    private final Container levelContainer;

    private final Container scoreContainer;

    private final Container rowsContainer;

    private final Font defaultFont;

    @Inject
    public TetrisRendererImpl(FontFactory fontFactory) {
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);
        tetrisContainer = createTetrisContainer();
        nextShapeContainer = createNextShapeContainer();
        levelContainer = createLevelContainer();
        scoreContainer = createScoreContainer();
        rowsContainer = createRowsContainer();
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
        Lists.newArrayList(tetrisContainer, nextShapeContainer, levelContainer,
            scoreContainer, rowsContainer).forEach(container -> renderContainer(graphics, container));

        renderBlocks(tetris, graphics);
        renderLevel(tetris, graphics);
        renderScore(tetris, graphics);
        renderRows(tetris, graphics);
        renderNextShape(tetris, graphics);
    }

    private void renderBlocks(Tetris tetris, Graphics2D graphics) {
        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, TetrisConstants.BLOCK_SIZE,
                    -TetrisConstants.BLOCK_SIZE));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        renderTextInContainer(graphics, String.valueOf(tetris.getScore()), scoreContainer);
    }

    private void renderRows(Tetris tetris, Graphics2D graphics) {
        renderTextInContainer(graphics, String.valueOf(tetris.getCompletedRows()), rowsContainer);
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        renderTextInContainer(graphics, String.valueOf(tetris.getLevel()), levelContainer);
    }

    private void renderNextShape(Tetris tetris, Graphics2D graphics) {
        Shape nextShape = tetris.getNextShape();
        Rectangle containerRectangle = nextShapeContainer.getRectangle();
        Rectangle boundingBox = nextShape.getBoundingBox();

        int x = containerRectangle.x * TetrisConstants.BLOCK_SIZE;
        int y = containerRectangle.y * TetrisConstants.BLOCK_SIZE;
        int width = containerRectangle.width * TetrisConstants.BLOCK_SIZE;
        int height = containerRectangle.height * TetrisConstants.BLOCK_SIZE;
        int dx = x + (width - boundingBox.width * TetrisConstants.BLOCK_SIZE) / 2;
        int dy = y + (height - boundingBox.height * TetrisConstants.BLOCK_SIZE) / 2;

        nextShape.getBlocks().stream().forEach(
            block -> renderBlock(graphics, block, dx, dy));
    }

    private void renderContainer(Graphics2D graphics, Container container) {
        Color color = container.getColor();
        Rectangle rectangle = container.getRectangle();

        graphics.setColor(color);

        int x = rectangle.x * TetrisConstants.BLOCK_SIZE;
        int y = rectangle.y * TetrisConstants.BLOCK_SIZE;
        int width = rectangle.width * TetrisConstants.BLOCK_SIZE;
        int height = rectangle.height * TetrisConstants.BLOCK_SIZE;

        graphics.drawLine(x, y, x + width - 1, y);
        graphics.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        graphics.drawLine(x, y, x, y + height - 1);
        graphics.drawLine(x + width - 1, y, x + width - 1, y + height - 1);

        String title = container.getTitle();

        if (title == null) {
            return;
        }

        graphics.setColor(container.getFontColor());
        graphics.setFont(container.getFont());

        FontMetrics fontMetrics = graphics.getFontMetrics();

        int textHeight = fontMetrics.getHeight();
        int textWidth = fontMetrics.stringWidth(title);
        int textX1 = x + (width - textWidth) / 2;
        int textY1 = y - (TetrisConstants.BLOCK_SIZE - textHeight);

        graphics.drawString(title, textX1, textY1);
    }

    private void renderBlock(Graphics2D graphics, Block block, int dx, int dy) {
        ShapeType shapeType = block.getShape().getShapeType();

        Image image = blockImageFactory.createBlockImage(shapeType);

        int dx1 = dx + block.getLocation().x * TetrisConstants.BLOCK_SIZE;
        int dx2 = dx1 + TetrisConstants.BLOCK_SIZE;
        int dy1 = dy + block.getLocation().y * TetrisConstants.BLOCK_SIZE;
        int dy2 = dy1 + TetrisConstants.BLOCK_SIZE;
        int sx2 = image.getWidth(null);
        int sy2 = image.getHeight(null);

        graphics.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, sx2, sy2, null);
    }

    private void renderTextInContainer(Graphics2D graphics, String text, Container container) {
        Rectangle rectangle = container.getRectangle();

        FontMetrics fontMetrics = graphics.getFontMetrics();

        int x = rectangle.x * TetrisConstants.BLOCK_SIZE;
        int y = rectangle.y * TetrisConstants.BLOCK_SIZE;
        int width = rectangle.width * TetrisConstants.BLOCK_SIZE;
        int textHeight = fontMetrics.getHeight();
        int textWidth = fontMetrics.stringWidth(text);
        int x1 = x + (width - textWidth) / 2;
        int y1 = y + TetrisConstants.BLOCK_SIZE - (TetrisConstants.BLOCK_SIZE - textHeight);

        graphics.setColor(scoreContainer.getFontColor());
        graphics.setFont(scoreContainer.getFont());
        graphics.drawString(text, x1, y1);
    }

    private Container createTetrisContainer() {
        Rectangle rectangle = new Rectangle(1, 1, TetrisConstants.WIDTH, TetrisConstants.HEIGHT - 2);

        return defaultContainerBuilder(rectangle).build();
    }

    private Container createLevelContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 2, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(LEVEL).build();
    }

    private Container createScoreContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 5, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(SCORE).build();
    }

    private Container createRowsContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 8, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(ROWS).build();
    }

    private Container createNextShapeContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 11, 6, 6);

        return defaultContainerBuilder(rectangle).setTitle(NEXT_SHAPE).build();
    }

    private Builder defaultContainerBuilder(Rectangle rectangle) {
        return Container.Builder.instance().setFont(defaultFont).setRectangle(rectangle);
    }
}
