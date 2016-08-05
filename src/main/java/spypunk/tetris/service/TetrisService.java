/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.service;

import java.util.Optional;

import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;

public interface TetrisService {

    void newGame(Tetris tetris);

    void update(Tetris tetris);

    void updateMovement(Tetris tetris, Optional<Movement> movement);

    void pause(Tetris tetris);
}
