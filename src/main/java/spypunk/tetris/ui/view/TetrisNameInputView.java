package spypunk.tetris.ui.view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Font;

import static spypunk.tetris.ui.constants.TetrisUIConstants.DEFAULT_FONT_COLOR;
import spypunk.tetris.ui.font.cache.FontCacheImpl;

public class TetrisNameInputView{

    private final JFrame frame;
    private final Container contentPane;
    private final JPanel firstPanel;
    private final JButton submitButton;
    private final JTextField textField;
    private final JTextArea textArea;

    private final FontCacheImpl fontCache;
    private final Font font;

    private String name="";
    public boolean isAdded=false;;

    public TetrisNameInputView(){

        frame=new JFrame();
        contentPane=frame.getContentPane();
        contentPane.setBackground(Color.BLACK);
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        firstPanel=new JPanel();
        textField = new JTextField(10);
        submitButton = new JButton("SUBMIT");
        textArea=new JTextArea();

        fontCache=new FontCacheImpl();
        font=fontCache.getDefaultFont();
    }

    public void show(int score){
        print(score);
        frame.setVisible(true);
    }

    private void print(int score){

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name=textField.getText();
                if(!name.equals("")){
                    if(isAdded){
                        isAdded=false;
                    }
                    frame.setVisible(false);
                }
            }
        });

        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(DEFAULT_FONT_COLOR);
        submitButton.setFont(font);

        textField.setBackground(Color.BLACK);
        textField.setForeground(DEFAULT_FONT_COLOR);
        textField.setFont(font);
        textField.setText("");

        textArea.setBackground(Color.BLACK);
        textArea.setForeground(DEFAULT_FONT_COLOR);
        textArea.setFont(font);
        
        textArea.setText("\n     YOU ARE IN TOP 5 !\n\n       SCORE : "+score+"\n\n  PLEASE ENTER YOUR NAME\n");
        textArea.setEditable(false);

        firstPanel.setBackground(Color.BLACK);
        firstPanel.add(textField);
        firstPanel.add(submitButton);
        
        frame.add(firstPanel,BorderLayout.CENTER);
        frame.add(textArea,BorderLayout.NORTH);
    }
    public String getName(){
        return name;
    }
   
}
