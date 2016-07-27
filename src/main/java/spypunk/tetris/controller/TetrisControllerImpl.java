package spypunk.tetris.controller;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.view.TetrisRenderer;

@Singleton
public class TetrisControllerImpl implements TetrisController {

    private static final int RENDER_PERIOD = 1000 / 60;

    private static final Map<Integer, Movement> MOVEMENTS = Maps.newHashMap();

    static {
        MOVEMENTS.put(KeyEvent.VK_DOWN, Movement.DOWN);
        MOVEMENTS.put(KeyEvent.VK_RIGHT, Movement.RIGHT);
        MOVEMENTS.put(KeyEvent.VK_LEFT, Movement.LEFT);
        MOVEMENTS.put(KeyEvent.VK_UP, Movement.ROTATE_CW);
    }

    @Inject
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    private TetrisRenderer tetrisRenderer;

    @Inject
    private TetrisService tetrisService;

    @Inject
    private TetrisFactory tetrisFactory;

    private volatile boolean newGame = true;

    private volatile Movement movement;

    private Future<?> loopThread;

    private Tetris tetris;

    @Override
    public void start() {
        tetrisRenderer.start();

        loopThread = scheduledExecutorService.scheduleAtFixedRate(() -> onGameLoop(), 0, RENDER_PERIOD,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public void onWindowClosed() {
        loopThread.cancel(false);
        scheduledExecutorService.shutdown();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        if (KeyEvent.VK_SPACE == keyCode) {
            newGame = true;
            return;
        }

        movement = MOVEMENTS.get(keyCode);
    }

    private void onGameLoop() {
        if (newGame) {
            tetris = tetrisFactory.createTetris();
            newGame = false;
        } else if (tetris == null) {
            return;
        } else {
            tetris.setMovement(Optional.ofNullable(movement));

            movement = null;

            tetrisService.update(tetris);
        }

        tetrisRenderer.render(tetris);
    }
}
