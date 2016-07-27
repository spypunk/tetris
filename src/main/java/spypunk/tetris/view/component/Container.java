package spypunk.tetris.view.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.util.SwingUtils;

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

        public Builder setColor(Color color) {
            container.setColor(color);
            return this;
        }

        public Builder setTitle(String title) {
            container.setTitle(title);
            return this;
        }

        public Builder setFont(Font font) {
            container.setFont(font);
            return this;
        }

        public Builder setFontColor(Color fontColor) {
            container.setFontColor(fontColor);
            return this;
        }

        public Container build() {
            return container;
        }
    }

    private Rectangle rectangle;

    private Color color = Color.GRAY;

    private String title;

    private Font font;

    private Color fontColor = Color.LIGHT_GRAY;

    public void render(Graphics2D graphics) {
        graphics.setColor(color);

        int x = rectangle.x * TetrisConstants.BLOCK_SIZE;
        int y = rectangle.y * TetrisConstants.BLOCK_SIZE;
        int width = rectangle.width * TetrisConstants.BLOCK_SIZE;
        int height = rectangle.height * TetrisConstants.BLOCK_SIZE;

        graphics.drawLine(x, y, x + width - 1, y);
        graphics.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
        graphics.drawLine(x, y, x, y + height - 1);
        graphics.drawLine(x + width - 1, y, x + width - 1, y + height - 1);

        if (title == null) {
            return;
        }

        graphics.setColor(fontColor);
        graphics.setFont(font);

        Point location = SwingUtils.getCenteredTextLocation(graphics, title,
            new Rectangle(rectangle.x, rectangle.y - 1, rectangle.width, 1));

        graphics.drawString(title, location.x, location.y);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
