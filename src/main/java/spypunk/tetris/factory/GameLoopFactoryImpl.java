package spypunk.tetris.factory;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import spypunk.tetris.gameloop.GameLoop;
import spypunk.tetris.gameloop.GameLoopImpl;
import spypunk.tetris.gameloop.GameLoopListener;

@Singleton
public class GameLoopFactoryImpl implements GameLoopFactory {

    @Override
    public GameLoop createGameLoop(GameLoopListener gameLoopListener) {
        return new GameLoopImpl(Executors.newSingleThreadExecutor(), gameLoopListener);
    }

}
