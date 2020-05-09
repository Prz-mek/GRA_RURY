package board;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import static javax.swing.SwingConstants.CENTER;

public class Board extends JFrame {
    int sx, sy, ex, ey; //punkt startowy, punkt końcowy (górny lewy róg to 0,0)
    int w = 0, h = 0;
    int[][] set;
    int now = 0;
    
    private JPanel grid;
    private JPanel select;
    private JPanel menu;
    
    private JButton[][] squares;
    
    //skróty oznaczają punkty zetknięcia rury z ramką pola; LR = left right, LD = left down
    private ImageIcon pipeLR = new ImageIcon("src/icons/rura.png");
    private ImageIcon pipeLD = new ImageIcon("src/icons/rurzysko.png");
    private ImageIcon pipeUD = new ImageIcon("src/icons/rurek.png");
    private ImageIcon pipeRD = new ImageIcon("src/icons/opadła.png");
    private ImageIcon pipeLU = new ImageIcon("src/icons/obrócona.png");
    private ImageIcon pipeRU = new ImageIcon("src/icons/wznosząca.png");
    
    ButtonHandler buttonHandler = new ButtonHandler();
    
    private void SaveLevel() {
        Scanner sc = new Scanner(System.in);
        try {
            File file;
            do {
                System.out.println("What should the save be called?\n");
                file = new File("src/saves/" + sc.next() + ".txt");
                if (file.exists()) {
                    System.out.println("Do you wish to this save? Type \"yes\" to confirm.");
                    if (sc.next().equals("yes")) {
                        break;
                    }
                }
            } while (file.exists());
                
            FileWriter save = new FileWriter(file.getAbsoluteFile());
            PrintWriter pw = new PrintWriter(save);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pw.print(set[i][j]);
                    if (j + 1 == w) {
                        pw.print("\n");
                    }
                }
            }
            pw.close();
            save.close();
        }
        catch (IOException e) {
            System.out.println("The file couldn't be created.");
        }
    }
    
    private void LoadLevel(String f) {
        try {
        File in = new File(f);
        Scanner sc = new Scanner(in);
        String tmp = "";
        while(sc.hasNext()) {
            h++;
            tmp = sc.nextLine();
        }
        w = tmp.length();
        if (h == 0) {
            System.out.println("Zapis jest pusty.");
        }
        sc = new Scanner(in);
        set = new int[h][w];
        for (int i = 0; i < h; i++) {
            tmp = sc.next();
            for (int j = 0; j < w; j++) {
                set[i][j] = tmp.charAt(j) - '0';
            }
        }
        sc.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Level loading error.");
        }
    }
    
    private void ChooseIcon(int n, int i, int j) {
        switch (n) {
            case 0: break;
            case 1: squares[i][j].setBackground(Color.BLACK); break;
            case 2: squares[i][j].setIcon(pipeLR); break;
            case 3: squares[i][j].setIcon(pipeUD); break;
            case 4: squares[i][j].setIcon(pipeRU); break;
            case 5: squares[i][j].setIcon(pipeLU); break;
            case 6: squares[i][j].setIcon(pipeLD); break;
            case 7: squares[i][j].setIcon(pipeRD); break;
        }
    }
    
    public static void main(String[] arg) {
        new Board("src/levels/Level1.txt");
    }
    
    int seconds = 0, minutes = 0;
    String time;
    
    private JLabel initLabel(String ch) {   //standard label format
        JLabel init = new JLabel();
        init.setBackground(Color.WHITE);
        init.setPreferredSize(new Dimension (60, 60));
        init.setText(ch);
        init.setOpaque(true);
        init.setHorizontalAlignment(CENTER);
        init.setBorder(BorderFactory.createLineBorder(new JButton().getBackground(), 1, true));
        return init;
    }
    
    public Board(String f) {
        super("Pipe Game");
        LoadLevel(f);
        squares = new JButton[h+1][w+1];
        select = new JPanel();
        select.setLayout(new GridLayout(h,2));
        select.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, Color.BLACK));
        grid = new JPanel();
        grid.setLayout(new GridLayout(h,w));
        grid.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new JButton().getBackground()));
        menu = new JPanel();
        menu.setLayout(new GridLayout(1,3));
        menu.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));

        for (int i = 0; i < h + 1; i++) {
            for (int j = 0; j < w + 1; j++) {
                int proxy = -1;
                squares[i][j] = new JButton();
                squares[i][j].setBackground(Color.WHITE);
                squares[i][j].addActionListener(buttonHandler);
                squares[i][j].setPreferredSize(new Dimension (60, 60));
                if (i == h) {
                    menu.add(squares[i][j]);
                    squares[i][j].setPreferredSize(new Dimension (0, 20));
                    switch (j) {
                        case 0: squares[i][j].setText("Save Game"); break;
                        case 1: menu.remove(squares[i][j]);
                        JLabel Stopwatch = initLabel("0");
                        Timer timer = new Timer(true);
                        TimerTask task = new TimerTask() {
                            public void run() {
                                seconds++;
                                if (seconds > 59) {
                                    seconds %= 60;
                                    minutes++;
                                }
                                if (minutes > 59)
                                    timer.cancel();
                                else if (minutes < 10 && seconds < 10)
                                    time = ("0" + minutes + ":0" + seconds);
                                else if (minutes < 10)
                                    time = ("0" + minutes + ":" + seconds);
                                else if (seconds < 10)
                                    time = (minutes + ":0" + seconds);
                                else
                                    time = (minutes + ":" + seconds);
                                Stopwatch.setText(time);
                            }
                        };
                        timer.scheduleAtFixedRate(task, 0, 1000);
                        menu.add(Stopwatch); break;
                        case 2: squares[i][j].setText("Quit to menu"); j = w;
                    }
                }
                else if (j < w) {
                    grid.add(squares[i][j]);
                    proxy = set[i][j];
                }
                else if (j == w){
                    select.add(initLabel("1"));
                    select.add(squares[i][j]);
                    proxy = i + 2;
                }
                ChooseIcon(proxy, i, j);
            }
        }
        
        getContentPane().add(grid, BorderLayout.CENTER);
        getContentPane().add(menu, BorderLayout.SOUTH);
        getContentPane().add(select, BorderLayout.EAST);
        setSize(60*(w+3),60*(h+1));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }
    
    private void GoToMenu() {
        this.dispose();
        new menu.MenuGUI().setVisible(true);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
        
            for (int i = 0; i < h + 1; i++) {
                for (int j = 0; j < w + 1; j++) {
                    if (source == squares[i][j]) {
                        if (i == h) {
                            if (j == 0) {
                                SaveLevel();
                            }
                            else {
                                GoToMenu();
                            }
                            return;
                        }
                        else if (j < w) {
                            processClick(i,j);
                            return;
                        }
                        else {
                            for (int k = 0; k < h; k++) {
                                if (k == i) {
                                    if (squares[k][j].getBackground() == Color.WHITE) {
                                        squares[k][j].setBackground(Color.YELLOW);
                                    }
                                    else {
                                        squares[k][j].setBackground(Color.WHITE);
                                        now = 0;
                                        return;
                                    }
                                }
                                else {
                                    squares[k][j].setBackground(Color.WHITE);
                                }
                            }
                            now = i + 2;
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void processClick(int i, int j) {
        if (set[i][j] == 0 && now != 0) {
            set[i][j] = now;
            ChooseIcon(now, i, j);
        }
    }
}
