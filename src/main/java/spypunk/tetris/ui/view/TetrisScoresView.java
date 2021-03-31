package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

public class TetrisScoresView {
    JFrame frame;
    public TetrisScoresView(){
        frame=new JFrame("High scores");
    }
    public void show(){
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
