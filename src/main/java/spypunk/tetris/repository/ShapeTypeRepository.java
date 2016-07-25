package spypunk.tetris.repository;

import java.util.List;

import spypunk.tetris.model.ShapeType;

@FunctionalInterface
public interface ShapeTypeRepository {

    List<ShapeType> findAll();

}
