package spypunk.tetris.factory;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spypunk.tetris.exception.TetrisException;

@Singleton
public class FontFactoryImpl implements FontFactory {

    private static final float DEFAULT_FONT_SIZE = 32F;

    private static final String DEFAULT_FONT_NAME = "default.ttf";

    private static final Logger LOGGER = LoggerFactory.getLogger(FontFactoryImpl.class);

    private static final File FONTS_FOLDER = new File("src/main/resources/font");

    private final Font defaultFont;

    public FontFactoryImpl() {
        try {
            defaultFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONTS_FOLDER, DEFAULT_FONT_NAME))
                    .deriveFont(DEFAULT_FONT_SIZE);
        } catch (FontFormatException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    @Override
    public Font createDefaultFont() {
        return defaultFont;
    }
}
