package spypunk.tetris.ui.view;

import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.factory.FontFactory;

public class TetrisURLViewImpl extends TetrisURLView {

    private static final class TetrisURLViewMouseAdapter extends MouseAdapter {

        private final TetrisController tetrisController;
        private final JLabel urlLabel;

        public TetrisURLViewMouseAdapter(TetrisController tetrisController, JLabel urlLabel) {
            this.tetrisController = tetrisController;
            this.urlLabel = urlLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            tetrisController.onURLClicked();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            urlLabel.setForeground(Color.CYAN);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            urlLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }

    private static final long serialVersionUID = -4190887625253783943L;

    @Inject
    public TetrisURLViewImpl(TetrisController tetrisController, FontFactory fontFactory) {
        final URI projectURI = tetrisController.getTetris().getProjectURI();

        final JLabel urlLabel = new JLabel(projectURI.getHost() + projectURI.getPath());

        final Font urlFont = fontFactory.createURLFont(10.0f);

        urlLabel.setFont(urlFont);
        urlLabel.setForeground(DEFAULT_FONT_COLOR);
        urlLabel.addMouseListener(new TetrisURLViewMouseAdapter(tetrisController, urlLabel));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setBackground(Color.BLACK);
        setOpaque(true);
        add(urlLabel, BorderLayout.EAST);
    }

    @Override
    public void update() {
        // Nothing to do here
    }

}
