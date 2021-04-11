package spypunk.tetris.ui.view;

import spypunk.tetris.guice.TetrisModule;
import spypunk.tetris.guice.TetrisModule2;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.ui.cache.ImageCache;
import spypunk.tetris.ui.controller.TetrisController;
import spypunk.tetris.ui.font.cache.FontCache;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Tetris2PlayerView extends AbstractView implements Runnable {

    private final JFrame frame = new JFrame("Tetris Co-op Mode");


    @Inject
    public Tetris2PlayerView(final TetrisController tetrisController1, final TetrisController tetrisController2,
                             final FontCache fontCache,
                             final ImageCache imageCache,
                             final @TetrisModule.TetrisProvider Tetris tetris1 , final @TetrisModule.TetrisProvider Tetris tetris2) {
        super(fontCache,imageCache,tetris1);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //frame.setResizable(false);
        frame.setSize(1600,800);


        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(tetrisController1.getJPanel(),BorderLayout.EAST);
        frame.add(tetrisController2.getJPanel(),BorderLayout.WEST);

        tetrisController1.start();
        tetrisController2.start();

        frame.addWindowListener(new TetrisMainView2Impl.TetrisViewWindowListener(tetrisController1));
        frame.addKeyListener(new TetrisMainView2Impl.TetrisViewKeyAdapter(tetrisController1));

        frame.addWindowListener(new TetrisMainView2Impl.TetrisViewWindowListener(tetrisController2));
        frame.addKeyListener(new TetrisMainView2Impl.TetrisViewKeyAdapter(tetrisController2));

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frame.repaint();
        }
    }
}
