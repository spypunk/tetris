package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JTextArea;


import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import spypunk.tetris.ui.font.cache.FontCacheImpl;

public class TetrisScoresView{

    JFrame frame;
    HashMap<String,Integer> lines;
    Container contentPane;

    public TetrisScoresView(){
        lines=new HashMap<>();

        frame=new JFrame("High scores");
        contentPane=frame.getContentPane();

        contentPane.setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        readScores();
        
    }
    public void show(){
        print();
        frame.setVisible(true);
    }
    public void print(){
        int counter=1;
        String s="";
        for ( String key : lines.keySet() ) {
            s+="\n  "+counter+") "+key+" "+lines.get(key);
            counter++;
        }
        System.out.println(s);
        final JTextArea textArea = new JTextArea(s);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(DEFAULT_FONT_COLOR);
        textArea.setEditable(false);
        
        FontCacheImpl obj=new FontCacheImpl();
        
        Font font=obj.getDefaultFont();
        float size=font.getSize()+8.0f;
        textArea.setFont(font.deriveFont(size));
        frame.add(textArea);
        
        
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
