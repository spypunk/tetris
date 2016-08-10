package spypunk.tetris.gameloop;

import java.util.concurrent.ExecutorService;

public final class GameLoopImpl implements GameLoop, Runnable {

    private static final int TICKS_PER_SECOND = 60;

    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

    private final ExecutorService executorService;

    private final GameLoopListener gameLoopListener;

    private volatile boolean running;

    public GameLoopImpl(ExecutorService executorService, GameLoopListener gameLoopListener) {
        this.executorService = executorService;
        this.gameLoopListener = gameLoopListener;
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
        long nextTick = System.currentTimeMillis();

        while (running) {
            for (; System.currentTimeMillis() > nextTick; nextTick += SKIP_TICKS) {
                gameLoopListener.onUpdate();
            }

            gameLoopListener.onRender();
        }
    }
}
