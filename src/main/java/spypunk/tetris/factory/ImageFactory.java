package spypunk.tetris.factory;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface ImageFactory {

    public Image createImage(File imageFile) throws IOException;
}
