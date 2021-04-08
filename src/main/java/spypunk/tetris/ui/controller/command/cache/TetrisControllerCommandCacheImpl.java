/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.controller.command.cache;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.view.TetrisMainView;

@Singleton
public class TetrisControllerCommandCacheImpl implements TetrisControllerCommandCache {

    private final TetrisService tetrisService;

    private final SoundService soundService;

    private final Tetris tetris;

    private final TetrisMainView tetrisMainView;

    private final Map<TetrisControllerCommandType, TetrisControllerCommand> tetrisControllerCommands = Maps
            .newHashMap();

    @Inject
    public TetrisControllerCommandCacheImpl(final TetrisService tetrisService,
            final SoundService soundService,
            @TetrisProvider final Tetris tetris,
            final TetrisMainView tetrisMainView) {
        this.tetrisService = tetrisService;
        this.soundService = soundService;
        this.tetris = tetris;
        this.tetrisMainView = tetrisMainView;

        tetrisControllerCommands.put(TetrisControllerCommandType.DOWN, createMoveCommand(Movement.DOWN));
        tetrisControllerCommands.put(TetrisControllerCommandType.LEFT, createMoveCommand(Movement.LEFT));
        tetrisControllerCommands.put(TetrisControllerCommandType.RIGHT, createMoveCommand(Movement.RIGHT));
        tetrisControllerCommands.put(TetrisControllerCommandType.ROTATE, createMoveCommand(Movement.ROTATE));
        tetrisControllerCommands.put(TetrisControllerCommandType.HARD_DROP, createHardDropCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.DECREASE_VOLUME, createDecreaseVolumeCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.INCREASE_VOLUME, createIncreaseVolumeCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.MUTE, createMuteCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.NEW_GAME, createNewGameCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.OPEN_PROJECT_URL, createOpenProjectURLCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.PAUSE, createPauseCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.SHAPE_LOCKED, createShapeLockedCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.GAME_OVER, createGameOverCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.ROWS_COMPLETED, createRowsCompletedCommand());
        tetrisControllerCommands.put(TetrisControllerCommandType.SHOW_SCORES, createShowScoresCommand());
        
    }

    @Override
    public TetrisControllerCommand getTetrisControllerCommand(
            final TetrisControllerCommandType tetrisControllerCommandType) {

        return tetrisControllerCommands.get(tetrisControllerCommandType);
    }

    private TetrisControllerCommand createNewGameCommand() {
        return () -> {
            tetrisService.start();
            soundService.playMusic(Sound.BACKGROUND);
        };
    }

    private TetrisControllerCommand createPauseCommand() {
        return () -> {
            tetrisService.pause();

            final State state = tetris.getState();

            if (State.PAUSED.equals(state)) {
                soundService.pauseMusic();
            } else if (State.RUNNING.equals(state)) {
                soundService.resumeMusic();
            }
        };
    }
    private TetrisControllerCommand createShowScoresCommand() {
        return () -> {
            tetrisService.showScores();

            final State state = tetris.getState();

            if (State.PAUSED.equals(state)) {
                soundService.pauseMusic();
            } else if (State.RUNNING.equals(state)) {
                soundService.resumeMusic();
            }
        };
    }

    private TetrisControllerCommand createMoveCommand(final Movement movement) {
        return () -> tetrisService.move(movement);
    }

    private TetrisControllerCommand createShapeLockedCommand() {
        return () -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    private TetrisControllerCommand createMuteCommand() {
        return () -> {
            tetrisService.mute();

            final boolean muted = tetris.isMuted();

            tetrisMainView.setMuted(muted);
            soundService.setMuted(muted);
        };
    }

    private TetrisControllerCommand createGameOverCommand() {
        return () -> {
            soundService.playMusic(Sound.GAME_OVER);
            tetrisService.takeName();
        };
    }

    private TetrisControllerCommand createRowsCompletedCommand() {
        return () -> soundService.playSound(Sound.ROWS_COMPLETED);
    }

    private TetrisControllerCommand createIncreaseVolumeCommand() {
        return soundService::increaseVolume;
    }

    private TetrisControllerCommand createDecreaseVolumeCommand() {
        return soundService::decreaseVolume;
    }

    private TetrisControllerCommand createHardDropCommand() {
        return tetrisService::hardDrop;
    }

    private TetrisControllerCommand createOpenProjectURLCommand() {
        return () -> SwingUtils.openURI(tetris.getProjectURI());
    }
}
