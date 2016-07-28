package spypunk.tetris.ui.factory;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.Rectangle;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.ui.model.Container;

@Singleton
public class ContainerFactoryImpl implements ContainerFactory {

    private static final int INFO_CONTAINERS_WIDTH = BLOCK_SIZE * 6;

    private static final int TETRIS_CONTAINER_WIDTH = 1 + BLOCK_SIZE * WIDTH;

    private static final int INFO_CONTAINERS_X = TETRIS_CONTAINER_WIDTH + 2 * BLOCK_SIZE;

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT SHAPE";

    private static final String ROWS = "ROWS";

    private final Container tetrisContainer;

    private final Container nextShapeContainer;

    private final Container levelContainer;

    private final Container scoreContainer;

    private final Container rowsContainer;

    @Inject
    public ContainerFactoryImpl(FontFactory fontFactory) {
        tetrisContainer = initializeTetrisContainer();
        nextShapeContainer = initializeNextShapeContainer();
        levelContainer = initializeLevelContainer();
        scoreContainer = initializeScoreContainer();
        rowsContainer = initializeRowsContainer();
    }

    @Override
    public Container createTetrisContainer() {
        return tetrisContainer;
    }

    @Override
    public Container createLevelContainer() {
        return levelContainer;
    }

    @Override
    public Container createScoreContainer() {
        return scoreContainer;
    }

    @Override
    public Container createRowsContainer() {
        return rowsContainer;
    }

    @Override
    public Container createNextShapeContainer() {
        return nextShapeContainer;
    }

    private Container initializeTetrisContainer() {
        return initializeContainer(BLOCK_SIZE, BLOCK_SIZE, TETRIS_CONTAINER_WIDTH,
            1 + BLOCK_SIZE * (HEIGHT - 2));
    }

    private Container initializeLevelContainer() {
        return initializeContainer(INFO_CONTAINERS_X, BLOCK_SIZE * 2, INFO_CONTAINERS_WIDTH, BLOCK_SIZE, LEVEL);
    }

    private Container initializeScoreContainer() {
        return initializeContainer(INFO_CONTAINERS_X, BLOCK_SIZE * 5, INFO_CONTAINERS_WIDTH, BLOCK_SIZE, SCORE);
    }

    private Container initializeRowsContainer() {
        return initializeContainer(INFO_CONTAINERS_X, BLOCK_SIZE * 8, INFO_CONTAINERS_WIDTH, BLOCK_SIZE, ROWS);
    }

    private Container initializeNextShapeContainer() {
        return initializeContainer(INFO_CONTAINERS_X, BLOCK_SIZE * 11, INFO_CONTAINERS_WIDTH, INFO_CONTAINERS_WIDTH,
            NEXT_SHAPE);
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
