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
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.google.common.collect.Lists;

import spypunk.tetris.model.Block;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;

@Singleton
public class ShapeFactoryImpl implements ShapeFactory {

    private static final int INITIAL_ROTATION = 0;

    private static final int MAX_START_X = 7;

    private final Random random = new Random();

    private final List<ShapeType> shapeTypes = Lists.newArrayList(ShapeType.values());

    @Override
    public Shape createRandomShape() {
        final ShapeType shapeType = getRandomShapeType();

        final int dx = random.nextInt(MAX_START_X);

        final Rectangle boundingBox = new Rectangle(shapeType.getBoundingBox());

        boundingBox.translate(dx, 0);

        final Shape shape = Shape.Builder.instance().setBoundingBox(boundingBox).setShapeType(shapeType)
                .setCurrentRotation(INITIAL_ROTATION).build();

        final List<Block> blocks = shapeType.getRotations().get(INITIAL_ROTATION).stream()
                .map(location -> new Point(location.x + dx, location.y))
                .map(location -> Block.Builder.instance().setLocation(location).setShape(shape).build())
                .collect(Collectors.toList());

        shape.setBlocks(blocks);

        return shape;
    }

    private ShapeType getRandomShapeType() {
        final int shapeIndex = random.nextInt(shapeTypes.size());

        return shapeTypes.get(shapeIndex);
    }
}
