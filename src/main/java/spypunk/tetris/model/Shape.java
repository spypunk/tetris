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

import com.google.common.collect.Lists;

public class Shape {

    private final ShapeType shapeType;

    private final Rectangle boundingBox;

    private final int currentRotation;

    private final List<Block> blocks = Lists.newArrayList();

    public class Block {

        private Point location;

        public Block(final Point location) {
            this.location = location;

            getShape().blocks.add(this);
        }

        public Shape getShape() {
            return Shape.this;
        }

        public Point getLocation() {
            return location;
        }

        public void setLocation(final Point location) {
            this.location = location;
        }
    }

    public Shape(final ShapeType shapeType, final Rectangle boundingBox, final int currentRotation) {
        this.shapeType = shapeType;
        this.boundingBox = boundingBox;
        this.currentRotation = currentRotation;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }
}
