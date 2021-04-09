package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    public HashMap<String,Integer> hashMap;
    private final Container contentPane;
    private final JTextArea textArea;
    private final FontCacheImpl fontCache;
    private final Font font ;
    private final int SIZE=5;

    public TetrisScoresView(){
        //Initilizations.
        frame=new JFrame();
        hashMap=new HashMap<>();

        contentPane=frame.getContentPane();
        contentPane.setBackground(Color.BLACK);

        frame.setPreferredSize(new Dimension(400, 300));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);

        textArea = new JTextArea();
        fontCache=new FontCacheImpl();
        font=fontCache.getDefaultFont();

        readFile();
    }

    public void show(){
        //Set visibility true.
        print();
        frame.setVisible(true);
    }

    private void print(){
        hashMap=sort(hashMap);
        int counter=1;
        String s="\n   HIGH SCORES\n";
        for ( String key : hashMap.keySet() ) {
            s+="\n   "+counter+") "+key+"     "+hashMap.get(key);
            counter++;
        }
        textArea.setText(s);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(DEFAULT_FONT_COLOR);
        textArea.setEditable(false);
        
        float size=font.getSize()+10.0f;
        textArea.setFont(font.deriveFont(size));
        frame.add(textArea);
    }

    private void update(){
        //When new element is added, sort the list and update the textArea.
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
        //Reads from file, separates name and score and add to list.
        try {
            File file = new File("score_table.txt");
            Scanner fileScanner= new Scanner(file);
            while (fileScanner.hasNextLine()) {
              String data = fileScanner.nextLine();
              String name=data.substring(0,data.indexOf(" "));
              String score_s=data.substring(data.indexOf(" ")+1);
              Integer score=Integer.parseInt(score_s);
              hashMap.put(name, score);
            }
            fileScanner.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    private void writeFile(){
        //Writes to file.
        try {
            FileWriter fileWriter = new FileWriter("score_table.txt");
            String str="";
            for ( String key : hashMap.keySet() ) {
                str+=key+" "+hashMap.get(key)+"\n";
            }
            fileWriter.write(str);
            fileWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    private HashMap<String,Integer> sort(HashMap<String,Integer> hashMap){
        //Sorts the list by using a new comparator.
        List<Map.Entry<String,Integer>>list=new LinkedList<Map.Entry<String,Integer>>(hashMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){
                
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        HashMap<String,Integer> sorted=new LinkedHashMap<>();
        for(Map.Entry<String,Integer > e:list){
            sorted.put(e.getKey(),e.getValue());
        }
        return sorted;
    }

    public Integer getMinScore(){
        //If list is empty then return 0.
        try {
            return Collections.min(hashMap.values());
        } catch (Exception e) {
            return 0;
        }
        
    }

    public void putScoreAndName(String name,Integer score){
        //If list is full then remove element has minimum score.
        if(hashMap.size()+1>SIZE)
            removeMinScoreElement();
        hashMap.put(name, score);
        update();
        writeFile();
    }

    public void removeMinScoreElement(){
        //Since the list is sorted when this is called, it removes the last element of the list. 
        try {
            Iterator<Entry<String, Integer>> itr = hashMap.entrySet().iterator();
            while(itr.hasNext())
                itr.next();
            itr.remove(); 
        } catch (Exception e) {
            System.err.println("Empty hashmap error");
        }
          
    }

    public boolean isFull(){
        return hashMap.size()==SIZE;
    }
}
