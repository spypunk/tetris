/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface ImageFactory {

    public Image createImage(InputStream inputStream) throws IOException;
}
