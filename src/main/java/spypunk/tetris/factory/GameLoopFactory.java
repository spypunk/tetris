package spypunk.tetris.factory;

import spypunk.tetris.gameloop.GameLoop;
import spypunk.tetris.gameloop.GameLoopListener;

@FunctionalInterface
public interface GameLoopFactory {

    GameLoop createGameLoop(GameLoopListener gameLoopListener);
}
