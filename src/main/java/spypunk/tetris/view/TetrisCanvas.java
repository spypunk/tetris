package spypunk.tetris.view;

import java.awt.Graphics2D;
import java.util.function.Consumer;

@FunctionalInterface
public interface TetrisCanvas {

    void render(Consumer<Graphics2D> consumer);
}
