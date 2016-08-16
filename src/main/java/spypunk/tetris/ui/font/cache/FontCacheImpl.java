/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.font.cache;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.ui.font.FontType;

@Singleton
public class FontCacheImpl implements FontCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontCacheImpl.class);

    private static final String FONTS_FOLDER = "/font/";

    private final Map<FontType, Font> fonts = loadFonts();

    @Override
    public Font getFont(FontType fontType) {
        return fonts.get(fontType);
    }

    private static Font createFont(FontType fontType) {
        final String resourceName = String.format("%s%s", FONTS_FOLDER, fontType.getFileName());

        try (InputStream inputStream = FontCacheImpl.class.getResourceAsStream(resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fontType.getSize());
        } catch (FontFormatException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private static Map<FontType, Font> loadFonts() {
        final Map<FontType, Font> fonts = Maps.newHashMap();

        for (final FontType fontType : FontType.values()) {
            fonts.put(fontType, createFont(fontType));
        }

        return fonts;
    }
}
