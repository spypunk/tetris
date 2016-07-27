package spypunk.tetris.factory;

import spypunk.tetris.view.component.Container;

public interface ContainerFactory {

    Container createTetrisContainer();

    Container createLevelContainer();

    Container createScoreContainer();

    Container createRowsContainer();

    Container createNextShapeContainer();

}
