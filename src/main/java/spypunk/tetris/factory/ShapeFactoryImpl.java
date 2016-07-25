package spypunk.tetris.factory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.repository.ShapeTypeRepository;

@Singleton
public class ShapeFactoryImpl implements ShapeFactory {

    private final Random random = new Random();

    private final List<ShapeType> shapeTypes;

    @Inject
    public ShapeFactoryImpl(ShapeTypeRepository shapeTypeRepository) {
        shapeTypes = shapeTypeRepository.findAll();
    }

    @Override
    public Shape createRandomShape() {
        ShapeType shapeType = getRandomShapeType();

        List<Set<Point>> rotations = shapeType.getRotations();

        int rotationIndex = random.nextInt(rotations.size());

        Set<Point> rotation = rotations.get(rotationIndex);

        Rectangle boundingBox = new Rectangle(shapeType.getBoundingBox());

        Shape shape = Shape.Builder.instance().setBoundingBox(boundingBox).setCurrentRotation(rotationIndex)
                .setShapeType(shapeType).build();

        List<Block> blocks = rotation.stream().map(location -> new Point(location.x, location.y))
                .map(location -> Block.Builder.instance().setLocation(location).setShape(shape).build())
                .collect(Collectors.toList());

        shape.setBlocks(blocks);

        return shape;
    }

    private ShapeType getRandomShapeType() {
        int shapeIndex = random.nextInt(shapeTypes.size());

        return shapeTypes.get(shapeIndex);
    }
}
