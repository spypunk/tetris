package spypunk.tetris.factory;

import java.awt.Font;

@FunctionalInterface
public interface FontFactory {

    Font createDefaultFont(float size);
}
