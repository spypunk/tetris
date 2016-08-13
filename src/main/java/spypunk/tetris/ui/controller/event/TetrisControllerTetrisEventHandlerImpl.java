package spypunk.tetris.ui.controller.event;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

@Singleton
public class TetrisControllerTetrisEventHandlerImpl implements TetrisControllerTetrisEventHandler {

    private final TetrisControllerCommandFactory tetrisControllerCommandFactory;

    @Inject
    public TetrisControllerTetrisEventHandlerImpl(TetrisControllerCommandFactory tetrisControllerCommandFactory) {
        this.tetrisControllerCommandFactory = tetrisControllerCommandFactory;
    }

    @Override
    public List<TetrisControllerCommand> handleEvents(List<TetrisEvent> tetrisEvents) {
        return tetrisEvents.stream().filter(TetrisEvent.SHAPE_LOCKED::equals)
                .map(tetrisEvent -> tetrisControllerCommandFactory.createShapeLockedTetrisControllerCommand())
                .collect(Collectors.toList());
    }

}
