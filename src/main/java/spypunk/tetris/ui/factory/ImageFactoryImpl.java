/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

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
