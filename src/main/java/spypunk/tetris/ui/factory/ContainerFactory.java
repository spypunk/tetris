package spypunk.tetris.ui.factory;

import spypunk.tetris.ui.model.Container;

public interface ContainerFactory {

    Container createTetrisContainer();

    Container createLevelContainer();

    Container createScoreContainer();

    Container createRowsContainer();

    Container createNextShapeContainer();

}
