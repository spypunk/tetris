package spypunk.tetris.ui.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.WindowEvent;

public class TetrisUserNameInputView extends JFrame {

    private String playerName = "";
    public boolean playerNameGiven = false;

    private JFrame jf = new JFrame();
    private JTextField userInput;

    public TetrisUserNameInputView() {

        JPanel panel = new JPanel();
        JLabel jl = new JLabel("Test");
        JButton jButton = new JButton("Click");

        userInput = new JTextField("", 30);

        jf.setSize(650, 200);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        jf.add(panel);
        panel.add(jl);
        panel.add(userInput);
        panel.add(jButton);


        jButton.addActionListener((e) -> {
            playerName = userInput.getText();
            playerNameGiven = true;
        });

    }

    public String getPlayerName(){
        System.out.println("1:" + playerName);
        return playerName;
    }

    public void closeView(){
        jf.dispatchEvent(new WindowEvent(jf, WindowEvent.WINDOW_CLOSING));
    }

}