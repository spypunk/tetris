/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Set;

import spypunk.tetris.model.Shape.Block;

public enum Movement {

    LEFT {
        @Override
        public Point apply(final Point location) {
            return new Point(location.x - 1, location.y);
        }
    },
    RIGHT {
        @Override
        public Point apply(final Point location) {
            return new Point(location.x + 1, location.y);
        }
    },
    DOWN {
        @Override
        public Point apply(final Point location) {
            return new Point(location.x, location.y + 1);
        }
    },
    ROTATE_CW {
        @Override
        public Shape apply(final Shape shape) {
            final Rectangle boundingBox = shape.getBoundingBox();
            final Rectangle newBoundingBox = new Rectangle(boundingBox);

            int nextRotationIndex = shape.getCurrentRotation() + 1;

            final List<Set<Point>> rotations = shape.getShapeType().getRotations();

            if (rotations.size() == nextRotationIndex) {
                nextRotationIndex = 0;
            }

            final Shape newShape = new Shape(shape.getShapeType(), newBoundingBox, nextRotationIndex);

            final Set<Point> rotation = rotations.get(nextRotationIndex);

            rotation.forEach(location -> newShape.new Block(
                    new Point(location.x + newBoundingBox.x, location.y + newBoundingBox.y)));

            return newShape;
        }

        @Override
        public Point apply(final Point location) {
            throw new UnsupportedOperationException();
        }
    };

    public abstract Point apply(Point location);

    public Shape apply(final Shape shape) {
        final Rectangle boundingBox = shape.getBoundingBox();

        final Rectangle newBoundingBox = new Rectangle(boundingBox);

        newBoundingBox.setLocation(apply(boundingBox.getLocation()));

        final Shape newShape = new Shape(shape.getShapeType(), newBoundingBox, shape.getCurrentRotation());

        shape.getBlocks().forEach(block -> apply(block, newShape));

        return newShape;
    }

    protected void apply(final Block block, final Shape newShape) {
        newShape.new Block(apply(block.getLocation()));
    }
}
