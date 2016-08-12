package spypunk.tetris.ui.factory;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.service.MusicService;

@Singleton
public class TetrisControllerCommandFactoryImpl implements TetrisControllerCommandFactory {

    private final TetrisControllerCommand newGameTetrisControllerCommand;

    private final TetrisControllerCommand pauseTetrisControllerCommand;

    private final Map<Movement, TetrisControllerCommand> movementTetrisControllerCommands;

    @Inject
    public TetrisControllerCommandFactoryImpl(TetrisService tetrisService, MusicService musicService) {
        newGameTetrisControllerCommand = tetris -> {
            tetrisService.newInstance(tetris);
            musicService.playMusic();
        };

        pauseTetrisControllerCommand = tetris -> {
            tetrisService.pauseInstance(tetris);
            musicService.pauseMusic();
        };

        movementTetrisControllerCommands = Maps.newHashMap();

        Lists.newArrayList(Movement.values()).stream()
                .forEach(movement -> movementTetrisControllerCommands.put(movement,
                    tetris -> tetrisService.updateInstanceMovement(tetris, movement)));
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
        return movementTetrisControllerCommands.get(movement);
    }
}
