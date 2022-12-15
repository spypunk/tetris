package spypunk.tetris.ui.view;

//import sun.text.resources.cldr.ext.FormatData_ia;

import com.google.inject.Guice;
import com.google.inject.Injector;
import spypunk.tetris.guice.TetrisModule2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class KeyConfigScreen {
    private JFrame mainFrame;

    private String[] keysForPlayer1;
    private String[] keysForPlayer2;

    public KeyConfigScreen() {

        mainFrame = new JFrame("Configure Key Bindings");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        //mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame,BoxLayout.PAGE_AXIS));


        mainPanel.add(new JLabel("Oyuncularin tuslarini ayarlayin. Tuslarda cakisma olursa oyun baslamaz!"));
        JLabel result = new JLabel("Tuslari girin:");
        mainPanel.add(result);

        mainPanel.add(new JLabel("Player 1: Left"));
        JTextField tf1Left = new JTextField();
        mainPanel.add(tf1Left);
        mainPanel.add(new JLabel("Player 1: Right"));
        JTextField tf1Right = new JTextField();
        mainPanel.add(tf1Right);
        mainPanel.add(new JLabel("Player 1: Down"));
        JTextField tf1Down = new JTextField();
        mainPanel.add(tf1Down);
        mainPanel.add(new JLabel("Player 1: Rotate"));
        JTextField tf1Rotate = new JTextField();
        mainPanel.add(tf1Rotate);
        mainPanel.add(new JLabel("Player 1: Hard Drop"));
        JTextField tf1HardDrop = new JTextField();
        mainPanel.add(tf1HardDrop);

        mainPanel.add(new JLabel("Player 2: Left"));
        JTextField tf2Left = new JTextField();
        mainPanel.add(tf2Left);
        mainPanel.add(new JLabel("Player 2: Right"));
        JTextField tf2Right = new JTextField();
        mainPanel.add(tf2Right);
        mainPanel.add(new JLabel("Player 2: Down"));
        JTextField tf2Down = new JTextField();
        mainPanel.add(tf2Down);
        mainPanel.add(new JLabel("Player 2: Rotate"));
        JTextField tf2Rotate = new JTextField();
        mainPanel.add(tf2Rotate);
        mainPanel.add(new JLabel("Player 2: Hard Drop"));
        JTextField tf2HardDrop = new JTextField();
        mainPanel.add(tf2HardDrop);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] allKeys = new String[10];

                allKeys[0] = tf1Left.getText();
                allKeys[1] = tf1Right.getText();
                allKeys[2] = tf1Down.getText();
                allKeys[3] = tf1Rotate.getText();
                allKeys[4] = tf1HardDrop.getText();

                allKeys[5] = tf2Left.getText();
                allKeys[6] = tf2Right.getText();
                allKeys[7] = tf2Down.getText();
                allKeys[8] = tf2Rotate.getText();
                allKeys[9] = tf2HardDrop.getText();

                boolean hasCollision = false;
                for (int i = 0; i < allKeys.length; i++) {
                    for (int j = i+1; j < allKeys.length; j++) {
                        if (allKeys[i].equals(allKeys[j])) {
                            hasCollision = true;
                            break;
                        }
                    }
                }

                if (!hasCollision) {
                    String[] keys1 = new String[5];
                    String[] keys2 = new String[5];

                    System.arraycopy(allKeys,0,keys1,0,5);
                    System.arraycopy(allKeys,5,keys2,0,5);

                    keysForPlayer1 = keys1;
                    keysForPlayer2 = keys2;

                    //tetrisControllerInputHandler classinda key bindingleri.
                    final Injector injector = Guice.createInjector(new TetrisModule2());
                    injector.getInstance(Tetris2PlayerView.class);
                    mainFrame.dispose();
                }
                else {
                    result.setText("Tuslarda cakisma oldu. Lutfen tekrar deneyin.");
                }
            }
        });

        mainPanel.add(okButton);

        mainFrame.getContentPane().add(mainPanel);

        mainFrame.setSize(600,600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }


}
