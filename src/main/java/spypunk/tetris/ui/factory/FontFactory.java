package spypunk.tetris.ui.factory;

import java.awt.Font;

@FunctionalInterface
public interface FontFactory {

    Font createDefaultFont(float defaultFontSize);
}
