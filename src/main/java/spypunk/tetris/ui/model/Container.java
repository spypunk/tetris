package spypunk.tetris.ui.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

public class Container {

    public static class Builder {

        private final Container container = new Container();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setRectangle(Rectangle rectangle) {
            container.setRectangle(rectangle);
            return this;
        }

        public Builder setTitle(String title) {
            container.setTitle(title);
            return this;
        }

        public Container build() {
            return container;
        }
    }

    private Rectangle rectangle;

    private String title;

    private List<Line> lines;

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;

        int x = rectangle.x;
        int y = rectangle.y;
        int width = rectangle.width - 1;
        int height = rectangle.height - 1;

        lines = Arrays.asList(Line.Builder.instance().setStartLocation(new Point(x, y))
                .setEndLocation(new Point(x + width, y)).build(),
            Line.Builder.instance().setStartLocation(new Point(x, y + height))
                    .setEndLocation(new Point(x + width, y + height)).build(),
            Line.Builder.instance().setStartLocation(new Point(x, y))
                    .setEndLocation(new Point(x, y + height)).build(),
            Line.Builder.instance().setStartLocation(new Point(x + width, y))
                    .setEndLocation(new Point(x + width, y + height)).build());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Line> getLines() {
        return lines;
    }
}
