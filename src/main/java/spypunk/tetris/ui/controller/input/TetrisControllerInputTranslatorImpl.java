package spypunk.tetris.ui.controller.input;

import java.awt.event.KeyEvent;
import java.util.Map;

import com.google.common.collect.Maps;

public class TetrisControllerInputTranslatorImpl implements TetrisControllerInputTranslator {

    private final Map<Integer, Runnable> pressedKeyHandlers = Maps.newHashMap();

    private final Map<Integer, Runnable> releasedKeyHandlers = Maps.newHashMap();

    public TetrisControllerInputTranslatorImpl(TetrisControllerInputHandler tetrisControllerInputHandler) {
        pressedKeyHandlers.put(KeyEvent.VK_LEFT, tetrisControllerInputHandler::onMoveLeft);
        pressedKeyHandlers.put(KeyEvent.VK_RIGHT, tetrisControllerInputHandler::onMoveRight);
        pressedKeyHandlers.put(KeyEvent.VK_DOWN, tetrisControllerInputHandler::onMoveDown);

        releasedKeyHandlers.put(KeyEvent.VK_SPACE, tetrisControllerInputHandler::onNewGame);
        releasedKeyHandlers.put(KeyEvent.VK_P, tetrisControllerInputHandler::onPause);
        releasedKeyHandlers.put(KeyEvent.VK_UP, tetrisControllerInputHandler::onRotate);
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
}
