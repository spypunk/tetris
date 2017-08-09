/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.input;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

@Singleton
public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private final Set<CommandType> triggeredCommands = Sets.newConcurrentHashSet();

    private final Map<Integer, CommandType> keyEventCommandTypes = Maps.newHashMap();

    private final Map<CommandType, TetrisControllerCommand> tetrisControllerCommands = Maps.newHashMap();

    private enum InputType {
        KEY_PRESSED,
        KEY_RELEASED,
        MOUSE_CLICKED
    }

    private enum CommandType {
        LEFT(InputType.KEY_PRESSED),
        RIGHT(InputType.KEY_PRESSED),
        DOWN(InputType.KEY_PRESSED),
        ROTATE(InputType.KEY_PRESSED),
        NEW_GAME(InputType.KEY_RELEASED),
        PAUSE(InputType.KEY_RELEASED),
        MUTE(InputType.KEY_RELEASED),
        DECREASE_VOLUME(InputType.KEY_RELEASED),
        INCREASE_VOLUME(InputType.KEY_RELEASED),
        HARD_DROP(InputType.KEY_RELEASED),
        OPEN_PROJECT_URL(InputType.MOUSE_CLICKED);

        private final InputType inputType;

        private CommandType(final InputType inputType) {
            this.inputType = inputType;
        }
    }

    @Inject
    public TetrisControllerInputHandlerImpl(final TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        keyEventCommandTypes.put(KeyEvent.VK_LEFT, CommandType.LEFT);
        keyEventCommandTypes.put(KeyEvent.VK_RIGHT, CommandType.RIGHT);
        keyEventCommandTypes.put(KeyEvent.VK_UP, CommandType.ROTATE);
        keyEventCommandTypes.put(KeyEvent.VK_DOWN, CommandType.DOWN);
        keyEventCommandTypes.put(KeyEvent.VK_SPACE, CommandType.NEW_GAME);
        keyEventCommandTypes.put(KeyEvent.VK_P, CommandType.PAUSE);
        keyEventCommandTypes.put(KeyEvent.VK_M, CommandType.MUTE);
        keyEventCommandTypes.put(KeyEvent.VK_PAGE_UP, CommandType.INCREASE_VOLUME);
        keyEventCommandTypes.put(KeyEvent.VK_PAGE_DOWN, CommandType.DECREASE_VOLUME);
        keyEventCommandTypes.put(KeyEvent.VK_CONTROL, CommandType.HARD_DROP);

        tetrisControllerCommands.put(CommandType.LEFT, tetrisControllerCommandFactory.createMoveCommand(Movement.LEFT));

        tetrisControllerCommands.put(CommandType.RIGHT,
            tetrisControllerCommandFactory.createMoveCommand(Movement.RIGHT));

        tetrisControllerCommands.put(CommandType.ROTATE,
            tetrisControllerCommandFactory.createMoveCommand(Movement.ROTATE_CW));

        tetrisControllerCommands.put(CommandType.DOWN, tetrisControllerCommandFactory.createMoveCommand(Movement.DOWN));

        tetrisControllerCommands.put(CommandType.NEW_GAME, tetrisControllerCommandFactory.createNewGameCommand());

        tetrisControllerCommands.put(CommandType.PAUSE, tetrisControllerCommandFactory.createPauseCommand());

        tetrisControllerCommands.put(CommandType.MUTE, tetrisControllerCommandFactory.createMuteCommand());

        tetrisControllerCommands.put(CommandType.INCREASE_VOLUME,
            tetrisControllerCommandFactory.createIncreaseVolumeCommand());

        tetrisControllerCommands.put(CommandType.DECREASE_VOLUME,
            tetrisControllerCommandFactory.createDecreaseVolumeCommand());

        tetrisControllerCommands.put(CommandType.HARD_DROP, tetrisControllerCommandFactory.createHardDropCommand());

        tetrisControllerCommands.put(CommandType.OPEN_PROJECT_URL,
            tetrisControllerCommandFactory.createOpenProjectURLCommand());
    }

    @Override
    public void onKeyPressed(final int keyCode) {
        onKey(keyCode, InputType.KEY_PRESSED);
    }

    @Override
    public void onKeyReleased(final int keyCode) {
        onKey(keyCode, InputType.KEY_RELEASED);
    }

    @Override
    public void onProjectURLClicked() {
        triggeredCommands.add(CommandType.OPEN_PROJECT_URL);
    }

    @Override
    public void handleInputs() {
        triggeredCommands.stream().map(tetrisControllerCommands::get).forEach(TetrisControllerCommand::execute);

        triggeredCommands.clear();
    }

    private void onKey(final int keyCode, final InputType inputType) {
        if (keyEventCommandTypes.containsKey(keyCode)) {

            final CommandType commandType = keyEventCommandTypes.get(keyCode);

            if (inputType.equals(commandType.inputType)) {
                triggeredCommands.add(commandType);
            }
        }
    }
}
