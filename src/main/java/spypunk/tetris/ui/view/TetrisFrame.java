package spypunk.tetris.ui.view;

import java.awt.Container;

public interface TetrisFrame {

    Container getContentPane();

    void pack();

    void setVisible(boolean visible);
}
