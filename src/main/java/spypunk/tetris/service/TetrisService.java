/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.service;

import spypunk.tetris.model.Movement;

public interface TetrisService {

    void start();

    void update();

    void move(Movement movement);

    void hardDrop();

    void pause();

    void mute();

    void showScores();

    void takeName();
    
}
