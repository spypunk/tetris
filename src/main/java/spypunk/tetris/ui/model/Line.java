package spypunk.tetris.ui.model;

import java.awt.Point;

public class Line {

    public static class Builder {

        private final Line line = new Line();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setStartLocation(Point startLocation) {
            line.setStartLocation(startLocation);
            return this;
        }

        public Builder setEndLocation(Point endLocation) {
            line.setEndLocation(endLocation);
            return this;
        }

        public Line build() {
            return line;
        }
    }

    private Point startLocation;

    private Point endLocation;

    public Point getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Point startLocation) {
        this.startLocation = startLocation;
    }

    public Point getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Point endLocation) {
        this.endLocation = endLocation;
    }
}
