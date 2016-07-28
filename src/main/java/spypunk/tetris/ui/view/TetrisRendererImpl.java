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

import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.factory.BlockImageFactory;
import spypunk.tetris.ui.factory.ContainerFactory;
import spypunk.tetris.ui.factory.FontFactory;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.model.Line;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisRendererImpl implements TetrisRenderer {

    private static final Color FONT_COLOR = Color.LIGHT_GRAY;

    private static final Color CONTAINER_COLOR = Color.GRAY;

    @Inject
    private TetrisFrame tetrisFrame;

    @Inject
    private TetrisCanvas tetrisCanvas;

    @Inject
    private BlockImageFactory blockImageFactory;

    @Inject
    private ContainerFactory containerFactory;

    private final Font defaulFont;

    @Inject
    public TetrisRendererImpl(FontFactory fontFactory) {
        defaulFont = fontFactory.createDefaultFont();
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
        renderContainer(graphics, containerFactory.createTetrisContainer());

        tetris.getBlocks().values().stream().filter(Optional::isPresent)
                .map(Optional::get)
                .filter(block -> block.getLocation().y >= 2)
                .forEach(block -> renderBlock(graphics, block, BLOCK_SIZE + 1,
                    -BLOCK_SIZE + 1));
    }

    private void renderScore(Tetris tetris, Graphics2D graphics) {
        Container scoreContainer = containerFactory.createScoreContainer();

        renderContainer(graphics, scoreContainer);
        renderTextCentered(graphics, String.valueOf(tetris.getScore()), scoreContainer.getRectangle());
    }

    private void renderRows(Tetris tetris, Graphics2D graphics) {
        Container rowsContainer = containerFactory.createRowsContainer();

        renderContainer(graphics, rowsContainer);
        renderTextCentered(graphics, String.valueOf(tetris.getCompletedRows()), rowsContainer.getRectangle());
    }

    private void renderLevel(Tetris tetris, Graphics2D graphics) {
        Container levelContainer = containerFactory.createLevelContainer();

        renderContainer(graphics, levelContainer);
        renderTextCentered(graphics, String.valueOf(tetris.getLevel()), levelContainer.getRectangle());
    }

    private void renderNextShape(Tetris tetris, Graphics2D graphics) {
        Container nextShapeContainer = containerFactory.createNextShapeContainer();

        renderContainer(graphics, nextShapeContainer);

        Shape nextShape = tetris.getNextShape();
        Rectangle containerRectangle = nextShapeContainer.getRectangle();
        Rectangle boundingBox = nextShape.getBoundingBox();

        int x = containerRectangle.x;
        int y = containerRectangle.y;
        int width = containerRectangle.width;
        int height = containerRectangle.height;
        int dx = x + (width - boundingBox.width * BLOCK_SIZE) / 2;
        int dy = y + (height - boundingBox.height * BLOCK_SIZE) / 2;

        nextShape.getBlocks().stream().forEach(
            block -> renderBlock(graphics, block, dx, dy));
    }

    private void renderBlock(Graphics2D graphics, Block block, int dx, int dy) {
        ShapeType shapeType = block.getShape().getShapeType();

        Image image = blockImageFactory.createBlockImage(shapeType);

        int dx1 = dx + block.getLocation().x * BLOCK_SIZE;
        int dx2 = dx1 + BLOCK_SIZE;
        int dy1 = dy + block.getLocation().y * BLOCK_SIZE;
        int dy2 = dy1 + BLOCK_SIZE;
        int sx2 = image.getWidth(null);
        int sy2 = image.getHeight(null);

        graphics.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, sx2, sy2, null);
    }

    private void renderTextCentered(Graphics2D graphics, String text, Rectangle rectangle) {
        graphics.setColor(FONT_COLOR);
        graphics.setFont(defaulFont);

        Point location = SwingUtils.getCenteredTextLocation(graphics, text, rectangle);

        graphics.drawString(text, location.x, location.y);
    }

    public void renderContainer(Graphics2D graphics, Container container) {
        graphics.setColor(CONTAINER_COLOR);

        container.getLines().forEach(line -> renderLine(graphics, line));

        String title = container.getTitle();

        if (title == null) {
            return;
        }

        graphics.setColor(FONT_COLOR);
        graphics.setFont(defaulFont);

        Rectangle rectangle = container.getRectangle();

        Point location = SwingUtils.getCenteredTextLocation(graphics, title,
            new Rectangle(rectangle.x, rectangle.y - BLOCK_SIZE, rectangle.width, BLOCK_SIZE));

        graphics.drawString(title, location.x, location.y);
    }

    private void renderLine(Graphics2D graphics, Line line) {
        Point startLocation = line.getStartLocation();
        Point endLocation = line.getEndLocation();

        graphics.drawLine(startLocation.x, startLocation.y, endLocation.x, endLocation.y);
    }
}
