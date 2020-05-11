package board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Scrollable extends JFrame {
    private JPanel main;
    private JButton[] buttons;
    private JButton more;
    private ButtonHandler buttonHandler;
    private JScrollPane list;
    
    public static void main(String[] arg) {
        new Scrollable();
    }
    
    public Scrollable() {
        super("Choose save");
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        buttons = new JButton[5];
        buttonHandler = new ButtonHandler();
        File[] loads = LoadSaves();
        for (int i = 0; i < loads.length; i++) {
            buttons[i] = new JButton(Types.GetName(loads[i]));
            buttons[i].addActionListener(buttonHandler);
            main.add(buttons[i]);
        }
        list = new JScrollPane(main);
        setContentPane(list);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setSize(400, 400);
        setVisible(true);
        pack();
    }
    
    File[] LoadSaves() {
        File open = new File("src/saves/");
        return open.listFiles();
    }
    
    private void Close() {
        this.dispose();
    }
    
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            int i = 0;
            for (JButton j : buttons) {
                if (j == source) {
                    new Board(LoadSaves()[i].getPath());
                    break;
                }
                else i++;
            }
            Close();
        }
    }
}
