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

    private final JFrame frame;
    private HashMap<String,Integer> hashMap;
    private final Container contentPane;
    private final JTextArea textArea;
    private final FontCacheImpl fontCache;
    private final Font font ;

    public TetrisScoresView(){
        frame=new JFrame();
        hashMap=new HashMap<>();

        contentPane=frame.getContentPane();
        contentPane.setBackground(Color.BLACK);

        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);

        textArea = new JTextArea();
        fontCache=new FontCacheImpl();
        font=fontCache.getDefaultFont();

        readFile();
    }

    public void show(){
        print();
        frame.setVisible(true);
    }

    private void print(){
        hashMap=sort(hashMap);
        int counter=1;
        String s="  HIGH SCORES\n";
        for ( String key : hashMap.keySet() ) {
            s+="\n  "+counter+") "+key+" "+hashMap.get(key);
            counter++;
        }
        textArea.setText(s);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(DEFAULT_FONT_COLOR);
        textArea.setEditable(false);
        
        float size=font.getSize()+8.0f;
        textArea.setFont(font.deriveFont(size));
        frame.add(textArea);
    }

    private void update(){
        hashMap=sort(hashMap);
        int counter=1;
        String s="  HIGH SCORES\n";
        for ( String key : hashMap.keySet() ) {
            s+="\n  "+counter+") "+key+" "+hashMap.get(key);
            counter++;
        }
        textArea.setText(s);
    }

    private void readFile(){
        try {
            File myObj = new File("score_table.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              String name=data.substring(0,data.indexOf(" "));
              String score_s=data.substring(data.indexOf(" ")+1);
              Integer score=Integer.parseInt(score_s);
              hashMap.put(name, score);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    private HashMap<String,Integer> sort(HashMap<String,Integer> hashMap){
        List<Map.Entry<String,Integer>>list=new LinkedList<Map.Entry<String,Integer>>(hashMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){
                
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        HashMap<String,Integer> temp=new LinkedHashMap<>();
        for(Map.Entry<String,Integer > e:list){
            temp.put(e.getKey(),e.getValue());
        }
        return temp;
    }

    public Integer getMinScore(){
       
        try {
            return Collections.min(hashMap.values());
        } catch (Exception e) {
            return 0;
        }
        
        
    }

    public void putScoreAndName(String name,Integer score){
        removeMinScoreElement();
        hashMap.put(name, score);
        update();
    }

    private void removeMinScoreElement(){
        try {
            Iterator<Entry<String, Integer>> itr = hashMap.entrySet().iterator();
            while(itr.hasNext())
                itr.next();

            itr.remove(); 
        } catch (Exception e) {
            System.err.println("Empty hashmap error");
        }
          
    }
}
