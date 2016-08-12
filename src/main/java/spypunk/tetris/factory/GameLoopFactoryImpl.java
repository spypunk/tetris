/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

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
