/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Rectangle;
import java.util.List;

public class Shape {

    public static class Builder {

        private final Shape shape = new Shape();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setShapeType(ShapeType shapeType) {
            shape.setShapeType(shapeType);
            return this;
        }

        public Builder setBlocks(List<Block> blocks) {
            shape.setBlocks(blocks);
            return this;
        }

        public Builder setBoundingBox(Rectangle boundingBox) {
            shape.setBoundingBox(boundingBox);
            return this;
        }

        public Builder setCurrentRotation(int currentRotation) {
            shape.setCurrentRotation(currentRotation);
            return this;
        }

        public Shape build() {
            return shape;
        }
    }

    private ShapeType shapeType;

    private Rectangle boundingBox;

    private int currentRotation;

    private List<Block> blocks;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }

}
