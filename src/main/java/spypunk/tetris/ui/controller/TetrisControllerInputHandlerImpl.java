package spypunk.tetris.ui.controller;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.model.Movement;
import spypunk.tetris.ui.controller.command.TetrisControllerCommand;
import spypunk.tetris.ui.factory.TetrisControllerCommandFactory;

public class TetrisControllerInputHandlerImpl implements TetrisControllerInputHandler {

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    private boolean movementTriggered;

    private Movement movement;

    private boolean pauseTriggered;

    private boolean newGameTriggered;

    @Inject
    private TetrisControllerCommandFactory tetrisControllerCommandFactory;

    public TetrisControllerInputHandlerImpl() {
        pressedKeyHandlers.put(KeyEvent.VK_LEFT, this::onMoveLeft);
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, this::onMoveRight);
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, this::onMoveDown);

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, this::onNewGame);
        releasedKeyHandlers.put(KeyEvent.VK_P, this::onPause);
        releasedKeyHandlers.put(KeyEvent.VK_UP, this::onRotate);
    }

    @Override
    public void reset() {
        movementTriggered = false;
        movement = null;
        pauseTriggered = false;
        newGameTriggered = false;
    }

    @Override
    public List<TetrisControllerCommand> handleInput() {

        final List<TetrisControllerCommand> tetrisControllerCommands = Lists.newArrayList();

        if (newGameTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createNewGameTetrisControllerCommand());
        }

        if (pauseTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createPauseTetrisControllerCommand());
        }

        if (movementTriggered) {
            tetrisControllerCommands
                    .add(tetrisControllerCommandFactory.createMovementTetrisControllerCommand(movement));
        }

        return tetrisControllerCommands;
    }

    @Override
    public void onKeyPressed(int keyCode) {
        onKeyEvent(pressedKeyHandlers, keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        onKeyEvent(releasedKeyHandlers, keyCode);
    }

    private void onKeyEvent(Map<Integer, Runnable> keyHandlers, int keyCode) {
        if (keyHandlers.containsKey(keyCode)) {
            keyHandlers.get(keyCode).run();
        }
    }

    private void onMoveLeft() {
        onMovement(Movement.LEFT);
    }

    private void onMoveRight() {
        onMovement(Movement.RIGHT);
    }

    private void onMoveDown() {
        onMovement(Movement.DOWN);
    }

    private void onRotate() {
        onMovement(Movement.ROTATE_CW);
    }

    private void onNewGame() {
        newGameTriggered = true;
    }

    private void onPause() {
        pauseTriggered = true;
    }

    private void onMovement(Movement movement) {
        movementTriggered = true;
        this.movement = movement;
    }
}
