/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;
import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import spypunk.tetris.ui.controller.TetrisController;

@Singleton
public class TetrisFrameImpl implements TetrisFrame {

    private final class GameFrameWindowListener extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private final class GameCanvasKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            tetrisController.onKeyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            tetrisController.onKeyReleased(e.getKeyCode());
        }
    }

    private static final String TITLE = "Tetris";

    private static final Dimension DEFAULT_DIMENSION = new Dimension(
            WIDTH * BLOCK_SIZE + 9 * BLOCK_SIZE,
            (HEIGHT - 2) * BLOCK_SIZE + 2 * BLOCK_SIZE);

    private final TetrisController tetrisController;

    private final JFrame frame;

    private final JLabel label;

    private final BufferedImage image;

    @Inject
    public TetrisFrameImpl(TetrisController tetrisController) {
        this.tetrisController = tetrisController;

        frame = new JFrame(TITLE);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new GameFrameWindowListener());

        image = new BufferedImage(DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height, BufferedImage.TYPE_INT_ARGB);

        label = new JLabel(new ImageIcon(image));
        label.setFocusable(true);
        label.addKeyListener(new GameCanvasKeyAdapter());

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public void render(Consumer<Graphics2D> consumer) {
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height);

        consumer.accept(graphics);

        graphics.dispose();

        label.repaint();
    }
}
