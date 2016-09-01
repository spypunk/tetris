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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

@Singleton
public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private final BitSet bitSet = new BitSet();

    private final Map<InputType, Supplier<TetrisControllerCommand>> inputTypeHandlers = Maps.newHashMap();

    private final List<InputType> inputTypes = Lists.newArrayList(InputType.values());

    private Movement movement;

    @Inject
    public TetrisControllerInputHandlerImpl(final TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        pressedKeyHandlers.put(KeyEvent.VK_LEFT, () -> onMovement(Movement.LEFT));
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, () -> onMovement(Movement.RIGHT));
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, () -> onMovement(Movement.DOWN));

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, () -> onInput(InputType.NEW_GAME));
        releasedKeyHandlers.put(KeyEvent.VK_P, () -> onInput(InputType.PAUSE));
        releasedKeyHandlers.put(KeyEvent.VK_UP, () -> onMovement(Movement.ROTATE_CW));
        releasedKeyHandlers.put(KeyEvent.VK_M, () -> onInput(InputType.MUTE));
        releasedKeyHandlers.put(KeyEvent.VK_PAGE_UP, () -> onInput(InputType.INCREASE_VOLUME));
        releasedKeyHandlers.put(KeyEvent.VK_PAGE_DOWN, () -> onInput(InputType.DECREASE_VOLUME));

        inputTypeHandlers.put(InputType.NEW_GAME, tetrisControllerCommandFactory::createNewGameTetrisControllerCommand);
        inputTypeHandlers.put(InputType.PAUSE, tetrisControllerCommandFactory::createPauseTetrisControllerCommand);
        inputTypeHandlers.put(InputType.MOVEMENT,
            () -> tetrisControllerCommandFactory.createMovementTetrisControllerCommand(movement));
        inputTypeHandlers.put(InputType.MUTE, tetrisControllerCommandFactory::createMuteTetrisControllerCommand);
        inputTypeHandlers.put(InputType.INCREASE_VOLUME,
            tetrisControllerCommandFactory::createIncreaseVolumeTetrisControllerCommand);
        inputTypeHandlers.put(InputType.DECREASE_VOLUME,
            tetrisControllerCommandFactory::createDecreaseVolumeTetrisControllerCommand);
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
    public List<TetrisControllerCommand> handleInputs() {
        return inputTypes.stream().filter(this::isInputTriggered)
                .map(inputType -> inputTypeHandlers.get(inputType).get()).collect(Collectors.toList());
    }

    @Override
    public void reset() {
        bitSet.clear();
        movement = null;
    }

    private void onInput(final InputType inputType) {
        bitSet.set(inputType.ordinal());
    }

    private void onMovement(final Movement movement) {
        onInput(InputType.MOVEMENT);
        this.movement = movement;
    }

    private boolean isInputTriggered(final InputType inputType) {
        return bitSet.get(inputType.ordinal());
    }

    private void onKeyEvent(final Map<Integer, Runnable> keyHandlers, final int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }
}
