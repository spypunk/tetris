package spypunk.tetris.ui.factory;

import java.awt.Font;
import java.awt.Rectangle;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.ui.model.Container;
import spypunk.tetris.ui.model.Container.Builder;

@Singleton
public class ContainerFactoryImpl implements ContainerFactory {

    private static final String SCORE = "SCORE";

    private static final String LEVEL = "LEVEL";

    private static final String NEXT_SHAPE = "NEXT SHAPE";

    private static final String ROWS = "ROWS";

    private final Container tetrisContainer;

    private final Container nextShapeContainer;

    private final Container levelContainer;

    private final Container scoreContainer;

    private final Container rowsContainer;

    private final Font defaultFont;

    @Inject
    public ContainerFactoryImpl(FontFactory fontFactory) {
        defaultFont = fontFactory.createDefaultFont();
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
        Rectangle rectangle = new Rectangle(1, 1, TetrisConstants.WIDTH, TetrisConstants.HEIGHT - 2);

        return defaultContainerBuilder(rectangle).build();
    }

    private Container initializeLevelContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 2, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(LEVEL).build();
    }

    private Container initializeScoreContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 5, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(SCORE).build();
    }

    private Container initializeRowsContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 8, 6, 1);

        return defaultContainerBuilder(rectangle).setTitle(ROWS).build();
    }

    private Container initializeNextShapeContainer() {
        Rectangle rectangle = new Rectangle(TetrisConstants.WIDTH + 2, 11, 6, 6);

        return defaultContainerBuilder(rectangle).setTitle(NEXT_SHAPE).build();
    }

    private Builder defaultContainerBuilder(Rectangle rectangle) {
        return Container.Builder.instance().setFont(defaultFont).setRectangle(rectangle);
    }
}
