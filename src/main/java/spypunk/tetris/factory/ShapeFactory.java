package spypunk.tetris.factory;

import spypunk.tetris.model.Shape;

@FunctionalInterface
public interface ShapeFactory {

    Shape createRandomShape();
}
