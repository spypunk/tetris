package spypunk.tetris.view;

import java.awt.Container;

public interface TetrisFrame {

    Container getContentPane();

    void pack();

    void setVisible(boolean visible);
}
