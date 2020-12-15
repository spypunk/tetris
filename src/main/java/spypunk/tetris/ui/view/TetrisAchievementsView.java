/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;
import spypunk.tetris.ui.util.SwingUtils.Text;

public class TetrisAchievementsView extends AbstractTetrisView {

    private static final String ACHOEVEMENTS = "ACHIEVEMENTS";

    private final Rectangle AchievementsRectangle;

    private final Text AchievementsTitleText;

    private class TetrisAchievement {

        private final Rectangle textRectangle;

        TetrisAchievement(Rectangle textRectangle) {
            this.textRectangle = textRectangle;

        }

        public void render(final Graphics2D graphics) {

            String value = "";
            for (String s: tetris.getAchievements()) {
                value += s + "\n";
            }

            Text AchievementText = new Text(value, fontCache.getDefaultFont());

            SwingUtils.renderCenteredText(graphics, textRectangle, AchievementText);
        }
    }

    public TetrisAchievementsView(final FontCache fontCache,
                                final ImageCache imageCache, final Tetris tetris) {
        super(fontCache, imageCache, tetris);

        AchievementsRectangle = new Rectangle(0,  BLOCK_SIZE, BLOCK_SIZE*24, BLOCK_SIZE*2);

        AchievementsTitleText = new Text(ACHOEVEMENTS, fontCache.getDefaultFont());

        initializeComponent(AchievementsRectangle.width + 1, AchievementsRectangle.height + BLOCK_SIZE + 1);
    }

    private TetrisAchievement createTetrisAchievement() {

        final Rectangle textRectangle = new Rectangle(
                BLOCK_SIZE*12, BLOCK_SIZE + 10, 10, 10);

        return new TetrisAchievement(textRectangle);
    }

    @Override
    protected void doPaint(final Graphics2D graphics) {
        SwingUtils.drawRectangleWithTitle(graphics, AchievementsRectangle, AchievementsTitleText);
        TetrisAchievement ta = createTetrisAchievement();
        ta.render(graphics);
    }
}
