/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.exception.TetrisException;

@Singleton
public class FontFactoryImpl implements FontFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontFactoryImpl.class);

    private static final String FONTS_FOLDER = "/font/";

    private static final String DEFAULT_FONT_NAME = "neutronium.ttf";

    private static final String URL_FONT_NAME = "russo_one.ttf";

    private final Font defaultFont = createFont(DEFAULT_FONT_NAME);

    private final Font urlFont = createFont(URL_FONT_NAME);

    @Override
    public Font createDefaultFont(float size) {
        return defaultFont.deriveFont(size);
    }

    @Override
    public Font createURLFont(float size) {
        return urlFont.deriveFont(size);
    }

    private static Font createFont(String fontName) {
        final String resourceName = String.format("%s%s", FONTS_FOLDER, fontName);

        try (InputStream inputStream = FontFactoryImpl.class.getResourceAsStream(resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }
}
