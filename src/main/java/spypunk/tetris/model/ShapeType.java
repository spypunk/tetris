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

public class ShapeType {

    public enum Id {
        O,
        I,
        J,
        L,
        S,
        T,
        Z
    }

    public static class Builder {

        private final ShapeType shapeType = new ShapeType();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setId(Id id) {
            shapeType.setId(id);
            return this;
        }

        public Builder setRotations(List<Set<Point>> rotations) {
            shapeType.setRotations(rotations);
            return this;
        }

        public Builder setBoundingBox(Rectangle boundingBox) {
            shapeType.setBoundingBox(boundingBox);
            return this;
        }

        public ShapeType build() {
            return shapeType;
        }
    }

    private Id id;

    private List<Set<Point>> rotations;

    private Rectangle boundingBox;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List<Set<Point>> getRotations() {
        return rotations;
    }

    public void setRotations(List<Set<Point>> rotations) {
        this.rotations = rotations;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }
}
