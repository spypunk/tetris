package spypunk.tetris.ui.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.Movement;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.command.MovementTetrisControllerCommand;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisControllerCommand newGameTetrisControllerCommand;

    private final TetrisControllerCommand pauseTetrisControllerCommand;

    private final TetrisService tetrisService;

    @Inject
    public TetrisControllerCommandFactoryImpl(TetrisService tetrisService) {
        this.tetrisService = tetrisService;

        newGameTetrisControllerCommand = tetrisService::newInstance;
        pauseTetrisControllerCommand = tetrisService::pauseInstance;
    }

    @Override
    public TetrisControllerCommand createNewGameTetrisControllerCommand() {
        return newGameTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createPauseTetrisControllerCommand() {
        return pauseTetrisControllerCommand;
    }

    @Override
    public TetrisControllerCommand createMovementTetrisControllerCommand(Movement movement) {
        return new MovementTetrisControllerCommand(tetrisService, movement);
    }
}
