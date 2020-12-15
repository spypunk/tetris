package spypunk.tetris.ui.view;


import java.awt.GraphicsConfiguration;
import javax.swing.*;

public class TetrisHighScoreView extends JFrame{

    GameEntry[] entities;

    public TetrisHighScoreView(){
        super("TetrisHighScoreView");

        Scores highScoreTable = new Scores();
        highScoreTable.updateEntries();
        entities = highScoreTable.entities();
        JList<GameEntry> subList = new JList<>(entities);

        setSize(200, 350);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel subLabel = new JLabel("High Score Table:");
        panel.add(subLabel);
        subList.setVisibleRowCount(10);
        JScrollPane scroller = new JScrollPane(subList);
        panel.add(scroller);
        add(panel);

        setVisible(true);
        setResizable(true);
    }
}
