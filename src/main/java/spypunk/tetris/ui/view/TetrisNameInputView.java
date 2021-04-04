package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;


public class TetrisNameInputView{

    JFrame frame;
    Container contentPane;

    public TetrisNameInputView(){
        frame=new JFrame();
        contentPane=frame.getContentPane();
        contentPane.setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
     
        
    }
    public void show(){
        print();
        frame.setVisible(true);
    }
    public void print(){
        
        
    }
   
}
