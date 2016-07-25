package spypunk.tetris.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import spypunk.tetris.controller.TetrisController;

@Singleton
public class TetrisFrameImpl implements TetrisFrame {

    private static final String TITLE = "Tetris";

    private final class GameFrameWindowListener extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private final TetrisController tetrisController;

    private final JFrame frame;

    @Inject
    public TetrisFrameImpl(TetrisController tetrisController) {
        this.tetrisController = tetrisController;

        frame = new JFrame(TITLE);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new GameFrameWindowListener());
    }

    @Override
    public Container getContentPane() {
        return frame.getContentPane();
    }

    @Override
    public void pack() {
        frame.pack();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
