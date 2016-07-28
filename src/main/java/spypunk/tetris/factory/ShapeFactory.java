package spypunk.tetris.factory;

import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;

public interface ShapeFactory {

    Shape createShape(ShapeType shapeType, int rotationIndex);

    Shape createRandomShape();
}
