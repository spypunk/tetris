package spypunk.tetris.ui.controller.command;

import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;

public class MovementTetrisControllerCommand implements TetrisControllerCommand {

    private final TetrisService tetrisService;

    private final Movement movement;

    public MovementTetrisControllerCommand(TetrisService tetrisService, Movement movement) {
        this.tetrisService = tetrisService;
        this.movement = movement;
    }

    @Override
    public void execute(Tetris tetris) {
        tetrisService.updateInstanceMovement(tetris, movement);
    }

}
