package spypunk.tetris;

import com.google.inject.Guice;
import com.google.inject.Injector;

import spypunk.tetris.controller.TetrisController;
import spypunk.tetris.guice.TetrisModule;

public class Main {

    private Main() {
        throw new IllegalAccessError();
    }

    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new TetrisModule());
        final TetrisController tetrisController = injector.getInstance(TetrisController.class);
        tetrisController.start();
    }

}
