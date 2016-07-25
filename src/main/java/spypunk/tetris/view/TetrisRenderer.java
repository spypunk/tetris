package spypunk.tetris.view;

import spypunk.tetris.model.Tetris;

public interface TetrisRenderer {

    void start();

    void render(Tetris tetris);
}
