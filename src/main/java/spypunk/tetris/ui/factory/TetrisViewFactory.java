package spypunk.tetris.ui.factory;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.view.TetrisView;

@FunctionalInterface
public interface TetrisViewFactory {

    TetrisView createTetrisView(Tetris tetris);
}
