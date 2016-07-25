package spypunk.tetris.controller;

public interface TetrisController {

    void start();

    void onWindowClosed();

    void onKeyPressed(int keyCode);
}
