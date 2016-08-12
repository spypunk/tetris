/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.input;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private boolean movementTriggered;

    private Movement movement;

    private boolean pauseTriggered;

    private boolean newGameTriggered;

    @Inject
    private TetrisControllerCommandFactory tetrisControllerCommandFactory;

    @Override
    public void onMoveLeft() {
        onMovement(Movement.LEFT);
    }

    @Override
    public void onMoveRight() {
        onMovement(Movement.RIGHT);
    }

    @Override
    public void onMoveDown() {
        onMovement(Movement.DOWN);
    }

    @Override
    public void onRotate() {
        onMovement(Movement.ROTATE_CW);
    }

    @Override
    public void onNewGame() {
        newGameTriggered = true;
    }

    @Override
    public void onPause() {
        pauseTriggered = true;
    }

    @Override
    public void reset() {
        movementTriggered = false;
        movement = null;
        pauseTriggered = false;
        newGameTriggered = false;
    }

    @Override
    public List<TetrisControllerCommand> handleInput() {

        final List<TetrisControllerCommand> tetrisControllerCommands = Lists.newArrayList();

        if (newGameTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createNewGameTetrisControllerCommand());
        } else if (pauseTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createPauseTetrisControllerCommand());
        } else if (movementTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMovementTetrisControllerCommand(movement));
        }

        return tetrisControllerCommands;
    }

    private void onMovement(Movement movement) {
        movementTriggered = true;
        this.movement = movement;
    }
}
