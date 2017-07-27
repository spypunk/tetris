/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Rectangle;
import java.util.List;

public class Shape {

    private ShapeType shapeType;

    private Rectangle boundingBox;

    private int currentRotation;

    private List<Block> blocks;

    public static final class Builder {

        private final Shape shape = new Shape();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setShapeType(final ShapeType shapeType) {
            shape.setShapeType(shapeType);
            return this;
        }

        public Builder setBlocks(final List<Block> blocks) {
            shape.setBlocks(blocks);
            return this;
        }

        public Builder setBoundingBox(final Rectangle boundingBox) {
            shape.setBoundingBox(boundingBox);
            return this;
        }

        public Builder setCurrentRotation(final int currentRotation) {
            shape.setCurrentRotation(currentRotation);
            return this;
        }

        public Shape build() {
            return shape;
        }
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(final ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<Block> blocks) {
        this.blocks = blocks;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(final Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(final int currentRotation) {
        this.currentRotation = currentRotation;
    }

}
