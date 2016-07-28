package spypunk.tetris.ui.model;

import java.awt.Rectangle;

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

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
