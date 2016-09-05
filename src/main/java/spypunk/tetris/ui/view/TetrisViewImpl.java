/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.BLOCK_SIZE;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.font.FontType;
import spypunk.tetris.ui.font.cache.FontCache;
import spypunk.tetris.ui.icon.Icon;
import spypunk.tetris.ui.util.SwingUtils;

public class TetrisViewImpl implements TetrisView {

    private final class TetrisViewWindowListener extends WindowAdapter {

        private final TetrisController tetrisController;

        TetrisViewWindowListener(final TetrisController tetrisController) {
            this.tetrisController = tetrisController;
        }

        @Override
        public void windowClosed(final WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private static final class TetrisViewKeyAdapter extends KeyAdapter {

        private final TetrisController tetrisController;

        TetrisViewKeyAdapter(final TetrisController tetrisController) {
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

        URLLabelMouseAdapter(final TetrisController tetrisController, final JLabel urlLabel) {
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

    private final TetrisInstanceStatisticsView tetrisInstanceStatisticsView;

    private final TetrisInstanceInfoView tetrisInstanceInfoView;

    private final AbstractTetrisInstanceView tetrisInstanceGridView;

    private final JLabel muteLabel;

    private final ImageIcon muteImageIcon;

    private final ImageIcon unmuteImageIcon;

    public TetrisViewImpl(final TetrisController tetrisController,
            final FontCache fontCache,
            final ImageCache imageCache,
            final Tetris tetris) {
        tetrisInstanceStatisticsView = new TetrisInstanceStatisticsView(fontCache, imageCache, tetris);
        tetrisInstanceInfoView = new TetrisInstanceInfoView(fontCache, imageCache, tetris);
        tetrisInstanceGridView = new TetrisInstanceGridView(fontCache, imageCache, tetris);

        muteImageIcon = new ImageIcon(imageCache.getIcon(Icon.MUTE));
        unmuteImageIcon = new ImageIcon(imageCache.getIcon(Icon.UNMUTE));

        final URI projectURI = tetris.getProjectURI();

        muteLabel = new JLabel(unmuteImageIcon);

        final JLabel urlLabel = new JLabel(projectURI.getHost() + projectURI.getPath());

        urlLabel.setFont(fontCache.getFont(FontType.URL));
        urlLabel.setForeground(DEFAULT_FONT_COLOR);
        urlLabel.addMouseListener(new URLLabelMouseAdapter(tetrisController, urlLabel));

        final JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        bottomPanel.setBackground(Color.BLACK);

        bottomPanel.add(muteLabel, BorderLayout.WEST);
        bottomPanel.add(urlLabel, BorderLayout.EAST);

        final JPanel centerPanel = new JPanel(new BorderLayout(BLOCK_SIZE, 0));

        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));

        centerPanel.add(tetrisInstanceGridView, BorderLayout.CENTER);
        centerPanel.add(tetrisInstanceStatisticsView, BorderLayout.WEST);
        centerPanel.add(tetrisInstanceInfoView, BorderLayout.EAST);

        frame = new JFrame(tetris.getName() + " " + tetris.getVersion());

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new TetrisViewWindowListener(tetrisController));
        frame.addKeyListener(new TetrisViewKeyAdapter(tetrisController));
        frame.setIconImage(imageCache.getIcon(Icon.ICON));

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
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

    @Override
    public void setMute(final boolean mute) {
        SwingUtils.doInAWTThread(() -> doSetMute(mute), false);
    }

    private void doSetMute(final boolean mute) {
        muteLabel.setIcon(mute ? muteImageIcon : unmuteImageIcon);
    }

    private void doUpdate() {
        tetrisInstanceGridView.update();
        tetrisInstanceStatisticsView.update();
        tetrisInstanceInfoView.update();
    }
}
