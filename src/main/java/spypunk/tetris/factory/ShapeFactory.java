/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.factory;

import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;

public interface ShapeFactory {

    Shape createShape(ShapeType shapeType, int rotationIndex);

    Shape createRandomShape();
}
