/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.font.FontType;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisViewImpl implements TetrisView {

    private final class TetrisViewWindowListener extends WindowAdapter {

        private final TetrisController tetrisController;

        public TetrisViewWindowListener(final TetrisController tetrisController) {
            this.tetrisController = tetrisController;
        }

        @Override
        public void windowClosed(final WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private static final class TetrisViewKeyAdapter extends KeyAdapter {

        private final TetrisController tetrisController;

        public TetrisViewKeyAdapter(final TetrisController tetrisController) {
            this.tetrisController = tetrisController;
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            tetrisController.onKeyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            tetrisController.onKeyReleased(e.getKeyCode());
        }
    }

    private static final class URLLabelMouseAdapter extends MouseAdapter {

        private final TetrisController tetrisController;
        private final JLabel urlLabel;

        public URLLabelMouseAdapter(final TetrisController tetrisController, final JLabel urlLabel) {
            this.tetrisController = tetrisController;
            this.urlLabel = urlLabel;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            tetrisController.onURLOpen();
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            urlLabel.setForeground(Color.CYAN);
        }

        @Override
        public void mouseExited(final MouseEvent e) {
            urlLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }

    private final JFrame frame;

    private final TetrisInstanceView tetrisInstanceView;

    public TetrisViewImpl(final TetrisController tetrisController,
            final TetrisInstanceView tetrisInstanceView,
            final FontCache fontCache,
            final Tetris tetris,
            final ImageCache imageCache) {
        this.tetrisInstanceView = tetrisInstanceView;

        final URI projectURI = tetris.getProjectURI();
        final JLabel urlLabel = new JLabel(projectURI.getHost() + projectURI.getPath());

        urlLabel.setFont(fontCache.getFont(FontType.URL));
        urlLabel.setForeground(DEFAULT_FONT_COLOR);
        urlLabel.addMouseListener(new URLLabelMouseAdapter(tetrisController, urlLabel));

        final JPanel urlPanel = new JPanel(new BorderLayout());

        urlPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        urlPanel.setBackground(Color.BLACK);
        urlPanel.setOpaque(true);

        urlPanel.add(urlLabel, BorderLayout.EAST);

        frame = new JFrame(tetris.getName() + " " + tetris.getVersion());

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new TetrisViewWindowListener(tetrisController));
        frame.addKeyListener(new TetrisViewKeyAdapter(tetrisController));
        frame.setIconImage(imageCache.getShapeImage(ShapeType.I));

        frame.add(tetrisInstanceView, BorderLayout.CENTER);
        frame.add(urlPanel, BorderLayout.SOUTH);
        frame.pack();

        frame.setLocationRelativeTo(null);
    }

    @Override
    public void show() {
        SwingUtils.doInAWTThread(() -> frame.setVisible(true), true);
    }

    @Override
    public void update() {
        SwingUtils.doInAWTThread(this::doUpdate, true);
    }

    private void doUpdate() {
        tetrisInstanceView.update();
    }
}
