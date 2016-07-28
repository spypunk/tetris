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

@Singleton
public class ShapeFactoryImpl implements ShapeFactory {

    private final Random random = new Random();

    private final List<ShapeType> shapeTypes;

    @Inject
    public ShapeFactoryImpl(ShapeTypeFactory shapeTypeFactory) {
        shapeTypes = shapeTypeFactory.createAll();
    }

    @Override
    public Shape createShape(ShapeType shapeType, int rotationIndex) {
        Set<Point> rotation = shapeType.getRotations().get(rotationIndex);

        Rectangle boundingBox = new Rectangle(shapeType.getBoundingBox());

        Shape shape = Shape.Builder.instance().setBoundingBox(boundingBox).setCurrentRotation(rotationIndex)
                .setShapeType(shapeType).build();

        List<Block> blocks = rotation.stream().map(location -> new Point(location.x, location.y))
                .map(location -> Block.Builder.instance().setLocation(location).setShape(shape).build())
                .collect(Collectors.toList());

        shape.setBlocks(blocks);

        return shape;
    }

    @Override
    public Shape createRandomShape() {
        ShapeType shapeType = getRandomShapeType();

        List<Set<Point>> rotations = shapeType.getRotations();

        int rotationIndex = random.nextInt(rotations.size());

        return createShape(shapeType, rotationIndex);
    }

    private ShapeType getRandomShapeType() {
        int shapeIndex = random.nextInt(shapeTypes.size());

        return shapeTypes.get(shapeIndex);
    }
}
