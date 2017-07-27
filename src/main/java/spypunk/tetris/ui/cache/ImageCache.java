/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.cache;

import java.awt.Image;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.ui.icon.Icon;

public interface ImageCache {

    Image getIcon(Icon icon);

    Image getBlockImage(ShapeType shapeType);

    Image getShapeImage(ShapeType shapeType);
}
