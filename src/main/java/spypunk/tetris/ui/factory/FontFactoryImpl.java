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

    private static final String DEFAULT_FONT_NAME = "default.ttf";

    private final Font defaultFont;

    public FontFactoryImpl() {
        final String resourceName = String.format("%s%s", FONTS_FOLDER, DEFAULT_FONT_NAME);

        try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
            defaultFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    @Override
    public Font createDefaultFont(float size) {
        return defaultFont.deriveFont(size);
    }
}
