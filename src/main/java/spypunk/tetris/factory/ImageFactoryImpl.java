package spypunk.tetris.factory;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Singleton;

@Singleton
public class ImageFactoryImpl implements ImageFactory {

    @Override
    public Image createImage(File imageFile) throws IOException {
        return ImageIO.read(imageFile);
    }
}
