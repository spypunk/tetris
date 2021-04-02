package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JLabel;

import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;

public class TetrisScoresView{

    JFrame frame;
    HashMap<String,Integer> lines;
    public TetrisScoresView(){
        frame=new JFrame("High scores");
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        lines=new HashMap<>();
        readScores();
        
    }
    public void show(){
        print();
        frame.setVisible(true);
    }
    public void print(){
        final JLabel firstLabel = new JLabel("Test");
        firstLabel.setForeground(DEFAULT_FONT_COLOR);
        frame.add(firstLabel);
    }
    public void readScores(){
        try {
            File myObj = new File("score_table.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              String name=data.substring(0,data.indexOf(" "));
              String score_s=data.substring(data.indexOf(" ")+1);
              Integer score=Integer.parseInt(score_s);
              lines.put(name, score);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
