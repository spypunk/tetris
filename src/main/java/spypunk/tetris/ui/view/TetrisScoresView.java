package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.swing.JTextArea;


import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import spypunk.tetris.ui.font.cache.FontCacheImpl;

public class TetrisScoresView{

    JFrame frame;
    HashMap<String,Integer> hm;
    Container contentPane;

    public TetrisScoresView(){
        hm=new HashMap<>();

        frame=new JFrame();
        contentPane=frame.getContentPane();
        contentPane.setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        readFile();
        
    }
    public void show(){
        print();
        frame.setVisible(true);
    }
    public void print(){
        hm=sort(hm);
        int counter=1;
        String s="  HIGH SCORES\n";
        for ( String key : hm.keySet() ) {
            s+="\n  "+counter+") "+key+" "+hm.get(key);
            counter++;
        }
        JTextArea textArea = new JTextArea(s);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(DEFAULT_FONT_COLOR);
        textArea.setEditable(false);
        
        FontCacheImpl fontCache=new FontCacheImpl();
        
        Font font=fontCache.getDefaultFont();
        float size=font.getSize()+8.0f;
        textArea.setFont(font.deriveFont(size));
        frame.add(textArea);
        
        
    }
    public void readFile(){
        try {
            File myObj = new File("score_table.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              String name=data.substring(0,data.indexOf(" "));
              String score_s=data.substring(data.indexOf(" ")+1);
              Integer score=Integer.parseInt(score_s);
              hm.put(name, score);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    public HashMap<String,Integer> sort(HashMap<String,Integer> hashMap){
        List<Map.Entry<String,Integer>>list=new LinkedList<Map.Entry<String,Integer>>(hashMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){
                
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
        });
        HashMap<String,Integer> temp=new LinkedHashMap<>();
        for(Map.Entry<String,Integer > aa:list){
            temp.put(aa.getKey(),aa.getValue());
        }
        return temp;
    }
    public Integer getMinScore(){
        Integer min = Collections.min(hm.values());
        return min;
    }
    public void putScoreAndName(String name,Integer score){
        removeMinScoreElement();
        hm.put(name, score);
        hm=sort(hm);
    }
    public void removeMinScoreElement(){
        Iterator<Entry<String, Integer>> itr = hm.entrySet().iterator();

        while(itr.hasNext()) {
            itr.next();
        }
        itr.remove(); 
            
    }
}
