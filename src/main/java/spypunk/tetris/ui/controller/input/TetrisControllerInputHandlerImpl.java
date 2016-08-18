/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.input;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private final TetrisControllerCommandFactory tetrisControllerCommandFactory;

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private boolean movementTriggered;

    private Movement movement;

    private boolean pauseTriggered;

    private boolean newGameTriggered;

    private boolean muteTriggered;

    @Inject
    public TetrisControllerInputHandlerImpl(TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        this.tetrisControllerCommandFactory = tetrisControllerCommandFactory;

        pressedKeyHandlers.put(KeyEvent.VK_LEFT, this::onMoveLeft);
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, this::onMoveRight);
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, this::onMoveDown);

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, this::onNewGame);
        releasedKeyHandlers.put(KeyEvent.VK_P, this::onPause);
        releasedKeyHandlers.put(KeyEvent.VK_UP, this::onRotate);
        releasedKeyHandlers.put(KeyEvent.VK_M, this::onMute);
    }

    @Override
    public void onKeyPressed(int keyCode) {
        onKeyEvent(pressedKeyHandlers, keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        onKeyEvent(releasedKeyHandlers, keyCode);
    }

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
    public void onMute() {
        muteTriggered = true;
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

        if (muteTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMuteTetrisControllerCommand());
        }

        reset();

        return tetrisControllerCommands;
    }

    private void onKeyEvent(Map<Integer, Runnable> keyHandlers, int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }

    private void onMovement(Movement movement) {
        movementTriggered = true;
        this.movement = movement;
    }

    private void reset() {
        movementTriggered = false;
        movement = null;
        pauseTriggered = false;
        newGameTriggered = false;
        muteTriggered = false;
    }
}
