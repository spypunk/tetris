/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.util.SwingUtils;

@Singleton
public class TetrisViewImpl implements TetrisView {

    private final class TetrisWindowListener extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
            tetrisController.onWindowClosed();
        }
    }

    private final JFrame frame;

    private final TetrisInstanceView tetrisInstanceView;

    private final TetrisURLView tetrisURLView;

    @Inject
    private final TetrisController tetrisController;

    @Inject
    public TetrisViewImpl(TetrisController tetrisController,
            TetrisInstanceView tetrisInstanceView,
            TetrisURLView tetrisURLView) {
        this.tetrisController = tetrisController;
        this.tetrisInstanceView = tetrisInstanceView;
        this.tetrisURLView = tetrisURLView;

        final Tetris tetris = tetrisController.getTetris();

        frame = new JFrame(tetris.getName() + " " + tetris.getVersion());

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(false);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.addWindowListener(new TetrisWindowListener());

        frame.add(tetrisInstanceView, BorderLayout.CENTER);
        frame.add(tetrisURLView, BorderLayout.SOUTH);
        frame.pack();

        frame.setLocationRelativeTo(null);
    }

    @Override
    public void show() {
        SwingUtils.doInAWTThread(() -> frame.setVisible(true), true);
    }

    @Override
    public void update() {
        SwingUtils.doInAWTThread(() -> doUpdate(), true);
    }

    private void doUpdate() {
        tetrisInstanceView.update();
        tetrisURLView.update();
    }
}
