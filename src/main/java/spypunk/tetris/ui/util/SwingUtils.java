/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.util;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.exception.TetrisException;

public class SwingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingUtils.class);

    private SwingUtils() {
        throw new IllegalAccessError();
    }

    public static void doInAWTThread(Runnable runnable) {
        doInAWTThread(runnable, false);
    }

    public static void doInAWTThread(Runnable runnable, boolean wait) {
        if (wait) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (final InvocationTargetException e) {
                LOGGER.error(e.getMessage(), e);
                throw new TetrisException(e);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error(e.getMessage(), e);
                throw new TetrisException(e);
            }
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public static Rectangle getCenteredImageRectangle(Image image, Rectangle rectangle, double ratio) {
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);

        final int targetWidth = (int) (imageWidth * ratio);
        final int targetHeight = (int) (imageHeight * ratio);

        final int x1 = rectangle.x + (rectangle.width - targetWidth) / 2;
        final int y1 = rectangle.y + (rectangle.height - targetHeight) / 2;

        return new Rectangle(x1, y1, targetWidth, targetHeight);
    }

    public static Rectangle getCenteredImageRectangle(Image image, Rectangle rectangle) {
        return getCenteredImageRectangle(image, rectangle, 1);
    }

    public static Rectangle getCenteredTextRectangle(Graphics2D graphics, String text, Rectangle rectangle, Font font) {
        final Rectangle textBounds = getTextBounds(graphics, text, font);

        final int x1 = rectangle.x + (rectangle.width - textBounds.width) / 2;
        final int y1 = rectangle.y + (rectangle.height + textBounds.height) / 2;

        return new Rectangle(x1, y1, textBounds.width, textBounds.height);
    }

    public static Rectangle getTextBounds(Graphics2D graphics, String text, Font font) {
        final FontRenderContext frc = graphics.getFontRenderContext();
        final GlyphVector gv = font.createGlyphVector(frc, text);

        return gv.getPixelBounds(null, 0, 0);
    }

    public static void drawImage(Graphics2D graphics, final Image image,
            final Rectangle rectangle) {
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);

        graphics.drawImage(image, rectangle.x, rectangle.y, rectangle.x + rectangle.width,
            rectangle.y + rectangle.height, 0, 0, imageWidth, imageHeight,
            null);
    }

    public static void openURI(URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (final IOException e) {
                LOGGER.warn("Cannot open following URL" + uri + " : " + e.getMessage(), e);
            }
        } else {
            LOGGER.warn("Your system does not support URL browsing, cannot open following URL : " + uri);
        }
    }
}
