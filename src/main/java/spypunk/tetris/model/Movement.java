/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
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
import java.util.stream.Collectors;

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

            final Shape newShape = Shape.Builder.instance().setBoundingBox(newBoundingBox)
                    .setCurrentRotation(nextRotationIndex).setShapeType(shape.getShapeType()).build();

            final Set<Point> rotation = rotations.get(nextRotationIndex);

            final List<Block> blocks = rotation.stream()
                    .map(location -> Block.Builder.instance()
                            .setLocation(new Point(location.x + newBoundingBox.x, location.y + newBoundingBox.y))
                            .setShape(newShape).build())
                    .collect(Collectors.toList());

            newShape.setBlocks(blocks);

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

        final Shape newShape = Shape.Builder.instance().setBoundingBox(newBoundingBox)
                .setCurrentRotation(shape.getCurrentRotation()).setShapeType(shape.getShapeType()).build();

        final List<Block> blocks = shape.getBlocks().stream().map(block -> apply(block, newShape))
                .collect(Collectors.toList());

        newShape.setBlocks(blocks);

        return newShape;
    }

    protected Block apply(final Block block, final Shape newShape) {
        return Block.Builder.instance().setLocation(apply(block.getLocation())).setShape(newShape).build();
    }
}
