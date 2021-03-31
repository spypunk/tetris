package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;


public class TetrisScoresView{

    JFrame frame;

    public TetrisScoresView(){
        frame=new JFrame("High scores");
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        
    }
    public void show(){
        print();
        frame.setVisible(true);
    }
    public void print(){
        final JLabel firstLabel = new JLabel("Test");
        firstLabel.setForeground(Color.BLACK);
        frame.add(firstLabel);
    }
}
