package spypunk.tetris.gameloop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TetrisGameLoop {

    private class TetrisGameLoopRunnable implements Runnable {

        private static final int TICKS_PER_SECOND = 60;

        private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

        private final TetrisGameLoopListener tetrisGameLoopListener;

        private volatile boolean running = true;

        public TetrisGameLoopRunnable(TetrisGameLoopListener tetrisGameLoopListener) {
            this.tetrisGameLoopListener = tetrisGameLoopListener;
        }

        @Override
        public void run() {
            double nextTick = System.currentTimeMillis();

            while (running) {
                for (; System.currentTimeMillis() > nextTick; nextTick += SKIP_TICKS) {
                    tetrisGameLoopListener.update();
                }

                tetrisGameLoopListener.render();
            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    private final ExecutorService executorService;

    private final TetrisGameLoopRunnable tetrisGameLoopRunnable;

    public TetrisGameLoop(TetrisGameLoopListener tetrisGameLoopListener) {
        executorService = Executors.newSingleThreadExecutor();
        tetrisGameLoopRunnable = new TetrisGameLoopRunnable(tetrisGameLoopListener);
    }

    public void start() {
        executorService.execute(tetrisGameLoopRunnable);
    }

    public void stop() {
        tetrisGameLoopRunnable.setRunning(false);
        executorService.shutdown();
    }
}
