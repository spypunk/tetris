package spypunk.tetris.factory;

import java.util.List;

import spypunk.tetris.model.ShapeType;

@FunctionalInterface
public interface ShapeTypeFactory {

    List<ShapeType> createAll();

}
