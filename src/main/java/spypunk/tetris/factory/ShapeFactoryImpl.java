/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

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
        final Rectangle boundingBox = new Rectangle(shapeType.getBoundingBox());

        final Shape shape = Shape.Builder.instance().setBoundingBox(boundingBox).setCurrentRotation(rotationIndex)
                .setShapeType(shapeType).build();

        final Set<Point> rotation = shapeType.getRotations().get(rotationIndex);

        final List<Block> blocks = rotation.stream().map(location -> new Point(location.x, location.y))
                .map(location -> Block.Builder.instance().setLocation(location).setShape(shape).build())
                .collect(Collectors.toList());

        shape.setBlocks(blocks);

        return shape;
    }

    @Override
    public Shape createRandomShape() {
        final ShapeType shapeType = getRandomShapeType();

        final List<Set<Point>> rotations = shapeType.getRotations();

        final int rotationIndex = random.nextInt(rotations.size());

        return createShape(shapeType, rotationIndex);
    }

    private ShapeType getRandomShapeType() {
        final int shapeIndex = random.nextInt(shapeTypes.size());

        return shapeTypes.get(shapeIndex);
    }
}
