/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
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
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.ui.font.FontType;

@Singleton
public class FontCacheImpl implements FontCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontCacheImpl.class);

    private static final String FONTS_FOLDER = "/font/";

    private final Map<FontType, Font> fonts = createFonts();

    private static Font createFont(final FontType fontType) {
        final String resourceName = String.format("%s%s", FONTS_FOLDER, fontType.getFileName());

        try (InputStream inputStream = FontCacheImpl.class.getResourceAsStream(resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fontType.getSize());
        } catch (FontFormatException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    private static Map<FontType, Font> createFonts() {
        return Lists.newArrayList(FontType.values()).stream()
                .collect(Collectors.toMap(Function.identity(), FontCacheImpl::createFont));
    }

    @Override
    public Font getDefaultFont() {
        return getFont(FontType.DEFAULT);
    }

    @Override
    public Font getFrozenFont() {
        return getFont(FontType.FROZEN);
    }

    @Override
    public Font getURLFont() {
        return getFont(FontType.URL);
    }

    private Font getFont(final FontType fontType) {
        return fonts.get(fontType);
    }
}
