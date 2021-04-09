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

import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCache;
import spypunk.tetris.ui.controller.command.cache.TetrisControllerCommandCache.TetrisControllerCommandType;

@Singleton
public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private final Set<TetrisControllerCommandType> triggeredCommands = Sets.newConcurrentHashSet();

    private final Map<Integer, TetrisControllerCommandType> pressedKeyEventCommandTypes = Maps.newHashMap();

    private final Map<Integer, TetrisControllerCommandType> releasedKeyEventCommandTypes = Maps.newHashMap();

    private final TetrisControllerCommandCache tetrisControllerCommandCache;

    @Inject
    public TetrisControllerInputHandlerImpl(final TetrisControllerCommandCache tetrisControllerCommandCache) {
        this.tetrisControllerCommandCache = tetrisControllerCommandCache;

        pressedKeyEventCommandTypes.put(KeyEvent.VK_LEFT, TetrisControllerCommandType.LEFT);
        pressedKeyEventCommandTypes.put(KeyEvent.VK_RIGHT, TetrisControllerCommandType.RIGHT);
        pressedKeyEventCommandTypes.put(KeyEvent.VK_DOWN, TetrisControllerCommandType.DOWN);

        releasedKeyEventCommandTypes.put(KeyEvent.VK_UP, TetrisControllerCommandType.ROTATE);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_SPACE, TetrisControllerCommandType.NEW_GAME);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_P, TetrisControllerCommandType.PAUSE);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_M, TetrisControllerCommandType.MUTE);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_PAGE_UP, TetrisControllerCommandType.INCREASE_VOLUME);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_PAGE_DOWN, TetrisControllerCommandType.DECREASE_VOLUME);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_CONTROL, TetrisControllerCommandType.HARD_DROP);
        releasedKeyEventCommandTypes.put(KeyEvent.VK_H, TetrisControllerCommandType.SHOW_SCORES);

    }

    @Override
    public void onKeyPressed(final int keyCode) {
        onKey(keyCode, pressedKeyEventCommandTypes);
    }

    @Override
    public void onKeyReleased(final int keyCode) {
        onKey(keyCode, releasedKeyEventCommandTypes);
    }

    @Override
    public void onProjectURLClicked() {
        triggeredCommands.add(TetrisControllerCommandType.OPEN_PROJECT_URL);
    }

    @Override
    public void handleInputs() {
        if (triggeredCommands.isEmpty()) {
            return;
        }

        triggeredCommands.stream()
                .map(tetrisControllerCommandCache::getTetrisControllerCommand)
                .forEach(TetrisControllerCommand::execute);

        triggeredCommands.clear();
    }

    private void onKey(final int keyCode, final Map<Integer, TetrisControllerCommandType> keyEventCommandTypes) {
        if (keyEventCommandTypes.containsKey(keyCode)) {
            final TetrisControllerCommandType commandType = keyEventCommandTypes.get(keyCode);

            triggeredCommands.add(commandType);
        }
    }
}
