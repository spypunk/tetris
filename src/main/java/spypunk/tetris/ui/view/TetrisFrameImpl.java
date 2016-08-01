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
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;
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
    }

    private static final String TITLE = "Tetris";

    private static final int BUFFER_STATEGIES = 3;

    private static final Dimension DEFAULT_DIMENSION = new Dimension(
            WIDTH * BLOCK_SIZE + 9 * BLOCK_SIZE,
            (HEIGHT - 2) * BLOCK_SIZE + 2 * BLOCK_SIZE);

    private final TetrisController tetrisController;

    private final BufferStrategy bufferStrategy;

    private final JFrame frame;

    @Inject
    public TetrisFrameImpl(TetrisController tetrisController) {
        this.tetrisController = tetrisController;

        frame = new JFrame(TITLE);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new GameFrameWindowListener());

        Canvas canvas = new Canvas();

        canvas.setPreferredSize(DEFAULT_DIMENSION);
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(true);
        canvas.addKeyListener(new GameCanvasKeyAdapter());

        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.pack();

        canvas.createBufferStrategy(BUFFER_STATEGIES);

        bufferStrategy = canvas.getBufferStrategy();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            frame.setVisible(false);
        }
    }

    @Override
    public void render(Consumer<Graphics2D> consumer) {
        do {
            do {
                final Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

                initializeGraphics(graphics);

                consumer.accept(graphics);

                graphics.dispose();
            } while (bufferStrategy.contentsRestored());

            bufferStrategy.show();

        } while (bufferStrategy.contentsLost());
    }

    private void initializeGraphics(final Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, DEFAULT_DIMENSION.width, DEFAULT_DIMENSION.height);
    }
}
