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

    private final TetrisControllerCommandFactory tetrisControllerCommandFactory;

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private final BitSet bitSet = new BitSet();

    private Movement movement;

    @Inject
    public TetrisControllerInputHandlerImpl(final TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        this.tetrisControllerCommandFactory = tetrisControllerCommandFactory;

        pressedKeyHandlers.put(KeyEvent.VK_LEFT, () -> onMovement(Movement.LEFT, InputType.MOVE_LEFT));
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, () -> onMovement(Movement.RIGHT, InputType.MOVE_RIGHT));
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, () -> onMovement(Movement.DOWN, InputType.MOVE_DOWN));

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, () -> onInput(InputType.NEW_GAME));
        releasedKeyHandlers.put(KeyEvent.VK_P, () -> onInput(InputType.PAUSE));
        releasedKeyHandlers.put(KeyEvent.VK_UP, () -> onMovement(Movement.ROTATE_CW, InputType.ROTATE_CW));
        releasedKeyHandlers.put(KeyEvent.VK_M, () -> onInput(InputType.MUTE));
        releasedKeyHandlers.put(KeyEvent.VK_PAGE_UP, () -> onInput(InputType.INCREASE_VOLUME));
        releasedKeyHandlers.put(KeyEvent.VK_PAGE_DOWN, () -> onInput(InputType.DECREASE_VOLUME));
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
    public List<TetrisControllerCommand> handleInput() {

        final List<TetrisControllerCommand> tetrisControllerCommands = Lists.newArrayList();

        if (isInputTriggered(InputType.NEW_GAME)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createNewGameTetrisControllerCommand());
        } else if (isInputTriggered(InputType.PAUSE)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createPauseTetrisControllerCommand());
        } else if (movement != null) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMovementTetrisControllerCommand(movement));
        }

        if (isInputTriggered(InputType.MUTE)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMuteTetrisControllerCommand());
        } else if (isInputTriggered(InputType.INCREASE_VOLUME)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createIncreaseVolumeTetrisControllerCommand());
        } else if (isInputTriggered(InputType.DECREASE_VOLUME)) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createDecreaseVolumeTetrisControllerCommand());
        }

        reset();

        return tetrisControllerCommands;
    }

    private void onInput(final InputType inputType) {
        bitSet.set(inputType.ordinal());
    }

    private void onMovement(final Movement movement, final InputType inputType) {
        this.movement = movement;
        onInput(inputType);
    }

    private boolean isInputTriggered(final InputType inputType) {
        return bitSet.get(inputType.ordinal());
    }

    private void onKeyEvent(final Map<Integer, Runnable> keyHandlers, final int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }

    private void reset() {
        bitSet.clear();
        movement = null;
    }
}
