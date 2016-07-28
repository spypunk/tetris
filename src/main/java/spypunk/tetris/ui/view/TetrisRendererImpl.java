package spypunk.tetris.ui.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.factory.BlockImageFactory;
import spypunk.tetris.ui.factory.ContainerFactory;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisRendererImpl implements TetrisRenderer {

    @Inject
    private TetrisFrame tetrisFrame;

    @Inject
    private TetrisCanvas tetrisCanvas;

    @Inject
    private BlockImageFactory blockImageFactory;

    @Inject
    private ContainerFactory containerFactory;

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
        renderContainer(graphics, containerFactory.createTetrisContainer());

        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, TetrisConstants.BLOCK_SIZE,
                    -TetrisConstants.BLOCK_SIZE));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        Container scoreContainer = containerFactory.createScoreContainer();

        renderContainer(graphics, scoreContainer);
        renderTextInContainer(graphics, String.valueOf(tetris.getScore()), scoreContainer);
    }

    private void renderRows(Tetris tetris, Graphics2D graphics) {
        Container rowsContainer = containerFactory.createRowsContainer();

        renderContainer(graphics, rowsContainer);
        renderTextInContainer(graphics, String.valueOf(tetris.getCompletedRows()), rowsContainer);
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        Container levelContainer = containerFactory.createLevelContainer();

        renderContainer(graphics, levelContainer);
        renderTextInContainer(graphics, String.valueOf(tetris.getLevel()), levelContainer);
    }

    private void renderNextShape(Tetris tetris, Graphics2D graphics) {
        Container nextShapeContainer = containerFactory.createNextShapeContainer();

        renderContainer(graphics, nextShapeContainer);

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
        graphics.setColor(container.getFontColor());
        graphics.setFont(container.getFont());

        Rectangle rectangle = container.getRectangle();

        Point location = SwingUtils.getCenteredTextLocation(graphics, text, rectangle);

        graphics.drawString(text, location.x, location.y);
    }

    public void renderContainer(Graphics2D graphics, Container container) {
        graphics.setColor(container.getColor());

        Rectangle rectangle = container.getRectangle();

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

        Point location = SwingUtils.getCenteredTextLocation(graphics, title,
            new Rectangle(rectangle.x, rectangle.y - 1, rectangle.width, 1));

        graphics.drawString(title, location.x, location.y);
    }
}
