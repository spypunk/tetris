/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.sound.Sound;
import spypunk.tetris.sound.service.SoundService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.view.TetrisMainView;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisService tetrisService;

    private final SoundService soundService;

    private final Tetris tetris;

    private final TetrisMainView tetrisMainView;

    @Inject
    public TetrisControllerCommandFactoryImpl(final TetrisService tetrisService,
            final SoundService soundService,
            @TetrisProvider final Tetris tetris,
            final TetrisMainView tetrisMainView) {
        this.tetrisService = tetrisService;
        this.soundService = soundService;
        this.tetris = tetris;
        this.tetrisMainView = tetrisMainView;
    }

    @Override
    public TetrisControllerCommand createNewGameCommand() {
        return () -> {
            tetrisService.start();
            soundService.playMusic(Sound.BACKGROUND);
        };
    }

    @Override
    public TetrisControllerCommand createPauseCommand() {
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

    @Override
    public TetrisControllerCommand createMoveCommand(final Movement movement) {
        return () -> tetrisService.move(movement);
    }

    @Override
    public TetrisControllerCommand createShapeLockedCommand() {
        return () -> soundService.playSound(Sound.SHAPE_LOCKED);
    }

    @Override
    public TetrisControllerCommand createMuteCommand() {
        return () -> {
            tetrisService.mute();

            final boolean muted = tetris.isMuted();

            tetrisMainView.setMuted(muted);
            soundService.setMuted(muted);
        };
    }

    @Override
    public TetrisControllerCommand createGameOverCommand() {
        return () -> soundService.playMusic(Sound.GAME_OVER);
    }

    @Override
    public TetrisControllerCommand createRowsCompletedCommand() {
        return () -> soundService.playSound(Sound.ROWS_COMPLETED);
    }

    @Override
    public TetrisControllerCommand createIncreaseVolumeCommand() {
        return soundService::increaseVolume;
    }

    @Override
    public TetrisControllerCommand createDecreaseVolumeCommand() {
        return soundService::decreaseVolume;
    }

    @Override
    public TetrisControllerCommand createHardDropCommand() {
        return tetrisService::hardDrop;
    }
}
