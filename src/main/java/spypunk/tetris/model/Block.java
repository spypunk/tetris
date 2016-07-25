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

        public Builder setShape(Shape shape) {
            block.setShape(shape);
            return this;
        }

        public Builder setLocation(Point location) {
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

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Block [location=" + location + "]";
    }
}
