/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Point;

public class Block {

    public static class Builder {

        private final Block block = new Block();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setShape(final Shape shape) {
            block.setShape(shape);
            return this;
        }

        public Builder setLocation(final Point location) {
            block.setLocation(location);
            return this;
        }

        public Block build() {
            return block;
        }
    }

    private Shape shape;

    private Point location;

    public Shape getShape() {
        return shape;
    }

    public void setShape(final Shape shape) {
        this.shape = shape;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(final Point location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Block [location=" + location + "]";
    }
}
