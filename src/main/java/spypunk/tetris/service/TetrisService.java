package spypunk.tetris.service;

import spypunk.tetris.model.Tetris;

@FunctionalInterface
public interface TetrisService {

    void update(Tetris tetris);
}
