/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.factory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import javax.inject.Singleton;

import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;

@Singleton
public class ShapeFactoryImpl implements ShapeFactory {

    private static final int INITIAL_ROTATION = 0;

    private static final int MAX_START_X = 7;

    private final Random random = new Random();

    private final ShapeType[] shapeTypes = ShapeType.values();

    @Override
    public Shape createRandomShape() {
        final ShapeType shapeType = getRandomShapeType();

        final int dx = random.nextInt(MAX_START_X);

        final Rectangle boundingBox = new Rectangle(shapeType.getBoundingBox());

        boundingBox.translate(dx, 0);

        final Shape shape = new Shape(shapeType, boundingBox, INITIAL_ROTATION);

        shapeType.getRotations().get(INITIAL_ROTATION)
                .forEach(location -> shape.new Block(new Point(location.x + dx, location.y)));

        return shape;
    }

    private ShapeType getRandomShapeType() {
        return shapeTypes[random.nextInt(shapeTypes.length)];
    }
}
