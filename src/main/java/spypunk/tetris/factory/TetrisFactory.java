package spypunk.tetris.factory;

import spypunk.tetris.model.Tetris;

@FunctionalInterface
public interface TetrisFactory {

    Tetris createTetris();
}
