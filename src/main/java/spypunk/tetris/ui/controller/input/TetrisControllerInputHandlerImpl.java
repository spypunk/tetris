/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.input;

import java.awt.event.KeyEvent;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

@Singleton
public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private enum InputType {
        MOVEMENT,
        PAUSE,
        NEW_GAME,
        MUTE
    }

    private final TetrisControllerCommandFactory tetrisControllerCommandFactory;

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private final BitSet bitSet = new BitSet();

    private Movement movement;

    @Inject
    public TetrisControllerInputHandlerImpl(final TetrisControllerCommandFactory tetrisControllerCommandFactory) {
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
    public void onKeyPressed(final int keyCode) {
        onKeyEvent(pressedKeyHandlers, keyCode);
    }

    @Override
    public void onKeyReleased(final int keyCode) {
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
        onInput(InputType.NEW_GAME);
    }

    @Override
    public void onPause() {
        onInput(InputType.PAUSE);
    }

    @Override
    public void onMute() {
        onInput(InputType.MOVEMENT);
    }

    @Override
    public List<TetrisControllerCommand> handleInput() {

        final List<TetrisControllerCommand> tetrisControllerCommands = Lists.newArrayList();

        if (isInputTriggered(InputType.NEW_GAME)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createNewGameTetrisControllerCommand());
        } else if (isInputTriggered(InputType.PAUSE)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createPauseTetrisControllerCommand());
        } else if (isInputTriggered(InputType.MOVEMENT)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMovementTetrisControllerCommand(movement));
        }

        if (isInputTriggered(InputType.MUTE)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMuteTetrisControllerCommand());
        }

        reset();

        return tetrisControllerCommands;
    }

    private boolean isInputTriggered(final InputType inputType) {
        return bitSet.get(inputType.ordinal());
    }

    private void onKeyEvent(final Map<Integer, Runnable> keyHandlers, final int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }

    private void onMovement(final Movement movement) {
        onInput(InputType.MOVEMENT);
        this.movement = movement;
    }

    private void reset() {
        bitSet.clear();
        movement = null;
    }

    private void onInput(final InputType inputType) {
        bitSet.set(inputType.ordinal());
    }
}
