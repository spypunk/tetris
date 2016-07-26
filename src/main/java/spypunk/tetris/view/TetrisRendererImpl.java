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

    private final Font defaultFont;

    @Inject
    public TetrisRendererImpl(FontFactory fontFactory) {
        defaultFont = fontFactory.createDefaultFont(DEFAULT_FONT_SIZE);
        tetrisContainer = createTetrisContainer();
        nextShapeContainer = createNextShapeContainer();
        levelContainer = createLevelContainer();
        scoreContainer = createScoreContainer();
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
        renderTetris(tetris, graphics);
        renderLevel(tetris, graphics);
        renderScore(tetris, graphics);
        renderNextShape(tetris, graphics);
    }

    private void renderTetris(Tetris tetris, Graphics2D graphics) {
        renderContainer(graphics, tetrisContainer);

        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, TetrisConstants.BLOCK_SIZE,
                    -TetrisConstants.BLOCK_SIZE));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        renderContainer(graphics, scoreContainer);
        renderTextInContainer(graphics, String.valueOf(tetris.getScore()), scoreContainer);
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        renderContainer(graphics, levelContainer);
        renderTextInContainer(graphics, String.valueOf(tetris.getLevel()), levelContainer);
    }

    private void renderNextShape(Tetris tetris, Graphics2D graphics) {
        renderContainer(graphics, nextShapeContainer);

        Shape nextShape = tetris.getNextShape();

        Rectangle containerRectangle = nextShapeContainer.getRectangle();

        Rectangle boundingBox = nextShape.getBoundingBox();

        int dx = containerRectangle.x + (containerRectangle.width - boundingBox.width * TetrisConstants.BLOCK_SIZE) / 2;
        int dy = containerRectangle.y
                + (containerRectangle.height - boundingBox.height * TetrisConstants.BLOCK_SIZE) / 2;

        nextShape.getBlocks().stream().forEach(
            block -> renderBlock(graphics, block, dx, dy));
    }

    private void renderContainer(Graphics2D graphics, Container container) {
        Color color = container.getColor();
        Rectangle rectangle = container.getRectangle();

        graphics.setColor(color);

        graphics.drawLine(rectangle.x, rectangle.y, rectangle.x + rectangle.width - 1, rectangle.y);
        graphics.drawLine(rectangle.x, rectangle.y + rectangle.height - 1, rectangle.x + rectangle.width - 1,
            rectangle.y + rectangle.height - 1);

        graphics.drawLine(rectangle.x, rectangle.y, rectangle.x, rectangle.y + rectangle.height - 1);
        graphics.drawLine(rectangle.x + rectangle.width - 1, rectangle.y, rectangle.x + rectangle.width - 1,
            rectangle.y + rectangle.height - 1);

        String title = container.getTitle();

        if (title == null) {
            return;
        }

        graphics.setColor(container.getFontColor());
        graphics.setFont(container.getFont());

        FontMetrics fontMetrics = graphics.getFontMetrics();

        int height = fontMetrics.getHeight();
        int width = fontMetrics.stringWidth(title);
        int textX1 = rectangle.x + (rectangle.width - width) / 2;
        int textY1 = rectangle.y - (TetrisConstants.BLOCK_SIZE - height);

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

        int height = fontMetrics.getHeight();
        int width = fontMetrics.stringWidth(text);
        int x1 = rectangle.x + (rectangle.width - width) / 2;
        int y1 = rectangle.y + TetrisConstants.BLOCK_SIZE - (TetrisConstants.BLOCK_SIZE - height);

        graphics.setColor(scoreContainer.getFontColor());
        graphics.setFont(scoreContainer.getFont());
        graphics.drawString(text, x1, y1);
    }

    private Container createTetrisContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.BLOCK_SIZE, TetrisConstants.BLOCK_SIZE,
                TetrisConstants.BLOCK_SIZE * TetrisConstants.WIDTH,
                TetrisConstants.BLOCK_SIZE * (TetrisConstants.HEIGHT - 2));

        return defaultContainerBuilder(rectangle).build();
    }

    private Container createNextShapeContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.BLOCK_SIZE * (TetrisConstants.WIDTH + 2),
                8 * TetrisConstants.BLOCK_SIZE, TetrisConstants.BLOCK_SIZE * 6, TetrisConstants.BLOCK_SIZE * 6);

        return defaultContainerBuilder(rectangle).setTitle(NEXT_SHAPE).build();
    }

    private Container createLevelContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.BLOCK_SIZE * (TetrisConstants.WIDTH + 2),
                2 * TetrisConstants.BLOCK_SIZE, TetrisConstants.BLOCK_SIZE * 6, TetrisConstants.BLOCK_SIZE * 1);

        return defaultContainerBuilder(rectangle).setTitle(LEVEL).build();
    }

    private Container createScoreContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.BLOCK_SIZE * (TetrisConstants.WIDTH + 2),
                5 * TetrisConstants.BLOCK_SIZE, TetrisConstants.BLOCK_SIZE * 6, TetrisConstants.BLOCK_SIZE * 1);

        return defaultContainerBuilder(rectangle).setTitle(SCORE).build();
    }

    private Builder defaultContainerBuilder(Rectangle rectangle) {
        return Container.Builder.instance().setFont(defaultFont).setRectangle(rectangle);
    }
}
