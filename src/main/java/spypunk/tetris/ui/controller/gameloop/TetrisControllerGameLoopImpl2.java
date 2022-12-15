/*
package spypunk.tetris.ui.controller.gameloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.ui.controller.event.TetrisControllerTetrisEventHandler;
import spypunk.tetris.ui.controller.input.TetrisControllerInputHandler;
import spypunk.tetris.ui.view.TetrisMainView;

import javax.inject.Inject;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TetrisControllerGameLoopImpl2 implements TetrisControllerGameLoop, Runnable{


    private static final Logger LOGGER = LoggerFactory.getLogger(TetrisControllerGameLoopImpl.class);

    private static final int TICKS_PER_SECOND = 60;

    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

    private final ExecutorService executorService;

    private final TetrisControllerInputHandler tetrisControllerInputHandler;

    private final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler;

    private final TetrisService tetrisService;
    //todo tetrismainvievimpl2 yu bindliyecekisn 2 kisilik icin.
    private final TetrisMainView tetrisMainView;

    private final JPanel currentPanel;

    private volatile boolean running;

    @Inject
    public TetrisControllerGameLoopImpl2(final TetrisService tetrisService,
                                        final TetrisControllerInputHandler tetrisControllerInputHandler,
                                        final TetrisControllerTetrisEventHandler tetrisControllerTetrisEventHandler,
                                        final TetrisMainView tetrisMainView) {

        this.tetrisService = tetrisService;
        this.tetrisControllerInputHandler = tetrisControllerInputHandler;
        this.tetrisControllerTetrisEventHandler = tetrisControllerTetrisEventHandler;
        this.tetrisMainView = tetrisMainView;
        this.currentPanel = tetrisMainView.getJPanel();

        executorService = Executors
                .newSingleThreadExecutor(runnable -> new Thread(runnable, "TetrisControllerGameLoop"));
    }

    @Override
    public void start() {
        running = true;
        executorService.execute(this);
    }

    @Override
    public void stop() {
        running = false;
        executorService.shutdown();
    }

    @Override
    public void run() {
        tetrisMainView.show();

        while (running) {
            long currentTick = System.currentTimeMillis();

            update();

            for (final long nextTick = currentTick + SKIP_TICKS; currentTick < nextTick; currentTick = System
                    .currentTimeMillis()) {
                waitMore();
            }
        }

        tetrisMainView.hide();
    }

    private void update() {
        tetrisControllerInputHandler.handleInputs();

        tetrisService.update();

        tetrisControllerTetrisEventHandler.handleEvents();

        tetrisMainView.update();
    }

    private void waitMore() {
        try {
            Thread.sleep(1);
        } catch (final InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            stop();
        }
    }

}
*/
