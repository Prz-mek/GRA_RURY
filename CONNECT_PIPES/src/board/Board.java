package board;

import frames.Popup;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import static javax.swing.SwingConstants.CENTER;
import menu.MenuGUI;
import player.Player;
import player.PlayersRanking;
import player.Saves;

public class Board extends JFrame {

    int w = 0, h = 0;   //width and height of board
    int[][] set;        //array containing information what is where on the board
    int now = 0;        //currently selected pipe
    Tile[][] tile;     //array with all tile
    Types type;
    int[] pipecount;    //how many pipes are available (pipe, turn)
    private int time = 0;       //how long it took to solve the level
    public Player player;
    private int nextLevel;

    private JPanel grid;
    private JPanel select;
    private JPanel menu;
    private JLabel[] labels;

    private JButton[][] squares;

    //letters symbolise where the pipe connects; LR = left right, LD = left down
    //letters are sorted alphabetically
    ButtonHandler buttonHandler = new ButtonHandler();

    private void LoadLevel(String f) {
        try {
            File in = new File(f);
            Scanner sc = new Scanner(in);
            String tmp = null;
            pipecount = new int[2];
            pipecount[0] = sc.nextInt();
            if (sc.hasNextInt()) 
                pipecount[1] = sc.nextInt();
            else
                pipecount[1] = 0;
            if (sc.hasNextInt()) time = sc.nextInt();
            if (in.getAbsolutePath().contains("src\\levels\\"))
                nextLevel = in.getName().charAt(5) - '0' + 1;
            else
                nextLevel = sc.nextInt();
            sc.nextLine();
            while (sc.hasNext()) {
                h++;
                tmp = sc.nextLine();
            }
            String[] arr = tmp.split("\\s+");
            w = arr.length;
            if (h == 0) {
                System.out.println("Zapis jest pusty.");
            }
            sc = new Scanner(in);
            sc.nextLine();
            set = new int[h][w];
            for (int i = 0; i < h; i++) {
                tmp = sc.nextLine();
                arr = tmp.split("\\s+");
                for (int j = 0; j < w; j++) {
                    set[i][j] = type.GetIndex(arr[j]);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Level loading error.");
        }
    }

    public int SaveToFile(String s, boolean override) {
        try {
            if (s.isBlank() && override == false) {
                return 2;
            }
            File file;
            file = new File("src/saves/" + s + ".txt");
            if (file.exists() && override == false) {
                return 1;
            }
            file.delete();
            file.createNewFile();
            FileWriter save = new FileWriter(file.getAbsoluteFile());
            PrintWriter pw = new PrintWriter(save);
            pw.print(labels[0].getText() + " " + labels[1].getText() + " " + time + " " + nextLevel + "\n");
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pw.print(type.type[set[i][j]] + " ");
                    if (j + 1 == w) {
                        pw.print("\n");
                    }
                }
            }
            pw.close();
            save.close();
        } catch (IOException e) {
            System.out.println("The file couldn't be created.");
        }
        return 0;
    }

    private ImageIcon ChooseIcon(String s) {
        if (s == null) {
            return null;
        } else if (s.equals("empty")) {
            return null;
        }
        return new ImageIcon("src/icons/" + s + ".png");
    }

    private JLabel initLabel(String ch) {   //standard label format
        JLabel init = new JLabel();
        init.setBackground(Color.WHITE);
        init.setPreferredSize(new Dimension(60, 60));
        init.setText(ch);
        init.setOpaque(true);
        init.setHorizontalAlignment(CENTER);
        init.setBorder(BorderFactory.createLineBorder(new JButton().getBackground(), 1, true));
        return init;
    }

    String timeString;
    Timer timer;

    private JLabel initTimer() {
        JLabel Stopwatch = initLabel("0");
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                time++;
                int minutes = time/60;
                int seconds = time%60;
                
                if (seconds > 59) {
                    seconds %= 60;
                    minutes++;
                }
                if (minutes > 59) {
                    timer.cancel();
                } else if (minutes < 10 && seconds < 10) {
                    timeString = ("0" + minutes + ":0" + seconds);
                } else if (minutes < 10) {
                    timeString = ("0" + minutes + ":" + seconds);
                } else if (seconds < 10) {
                    timeString = (minutes + ":0" + seconds);
                } else {
                    timeString = (minutes + ":" + seconds);
                }
                Stopwatch.setText(timeString);
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
        return Stopwatch;
    }

    private void GenerateTiles() {
        tile = new Tile[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tile[i][j] = new Tile(type.type[set[i][j]]);
                tile[i][j].x = j;
                tile[i][j].y = i;
            }
        }
    }
    
    public Board(String f) {
        super("Pipe Game");
        type = new Types();
        LoadLevel(f);
        GenerateTiles();
        squares = new JButton[h + 1][w + 1];
        labels = new JLabel[3];
        select = new JPanel();
        select.setLayout(new GridLayout(h, 2));
        select.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, Color.BLACK));
        grid = new JPanel();
        grid.setLayout(new GridLayout(h, w));
        grid.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new JButton().getBackground()));
        menu = new JPanel();
        menu.setLayout(new GridLayout(1, 3));
        menu.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));

        for (int i = 0; i < h + 1; i++) {
            for (int j = 0; j < w + 1; j++) {
                String name = null;
                squares[i][j] = new JButton();
                squares[i][j].setBackground(Color.WHITE);
                squares[i][j].addActionListener(buttonHandler);
                squares[i][j].setPreferredSize(new Dimension(60, 60));
                if (i == h) {
                    menu.add(squares[i][j]);
                    squares[i][j].setPreferredSize(new Dimension(0, 20));
                    switch (j) {
                        case 0:
                            squares[i][j].setText("Save Game");
                            break;
                        case 1:
                            menu.remove(squares[i][j]);
                            menu.add(initTimer());
                            break;
                        case 2:
                            squares[i][j].setText("Quit to menu");
                            j = w;
                    }
                    continue;
                } else if (j < w) {
                    grid.add(squares[i][j]);
                    name = type.type[set[i][j]];
                } else if (j == w && i < 3) {
                    if (i == 2) {
                        labels[i] = initLabel(Character.toString('X'));
                    } else {
                        labels[i] = initLabel(Character.toString(pipecount[i] + '0'));
                    }
                    select.add(labels[i]);
                    select.add(squares[i][j]);
                    switch (i) {
                        case 0:
                            name = "1pipeLR";
                            break;
                        case 1:
                            name = "1turnLU";
                            break;
                        case 2:
                            name = "eraser";
                            break;
                    }
                }
                squares[i][j].setIcon(ChooseIcon(name));
            }
        }

        checkIfNull();
        getContentPane().add(grid, BorderLayout.CENTER);
        getContentPane().add(menu, BorderLayout.SOUTH);
        getContentPane().add(select, BorderLayout.EAST);
        setSize(60 * (w + 3), 60 * (h + 1));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private int[] ScanBoard() {
        int[] tmp = new int[2];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (tile[i][j].type.contains("start")) {
                    tmp[0] = j;
                    tmp[1] = i;
                    return tmp;
                }
            }
        }
        return null;
    }

    private boolean CheckIfFinished() {
        var start = ScanBoard();
        int prex = -1, prey = -1;
        if (start == null) {
            System.out.println("Error. This level doesn't have a start!");
            return false;
        }
        int x = start[0], y = start[1];
        int error = 0;
        while (true) {
            error++;
            if (error > 50) {
                System.out.println("Zapobiegam nieskończonej pętli.");
                return false;
            }
            if (tile[y][x].type.contains("finish")) {
                return true;
            } else if (prex != x + 1 && x + 1 < w && tile[y][x].IsConnectedTo(tile[y][x + 1])) {
                prex = x;
                prey = y;
                x++;
            } else if (prey != y + 1 && y + 1 < h && tile[y][x].IsConnectedTo(tile[y + 1][x])) {
                prex = x;
                prey = y;
                y++;
            } else if (prex != x - 1 && x - 1 >= 0 && tile[y][x].IsConnectedTo(tile[y][x - 1])) {
                prex = x;
                prey = y;
                x--;
            } else if (prey != y - 1 && y - 1 >= 0 && tile[y][x].IsConnectedTo(tile[y - 1][x])) {
                prex = x;
                prey = y;
                y--;
            } else {
                return false;
            }
        }
    }

    private void GoToMenu() {
        this.dispose();
        MenuGUI l = new menu.MenuGUI();
        l.setVisible(true);
        l.p = player;
    }

    private void Rotate(Tile t) {
        String s = null;
        switch (t.type) {
            case "1pipeLR":
                s = "1pipeDU";
                break;
            case "1pipeDU":
                s = "1pipeLR";
                break;
            case "1turnDL":
                s = "1turnLU";
                break;
            case "1turnLU":
                s = "1turnRU";
                break;
            case "1turnRU":
                s = "1turnDR";
                break;
            case "1turnDR":
                s = "1turnDL";
        }
        int x = t.x, y = t.y;
        t = new Tile(s);
        t.x = x;
        t.y = y;
        tile[y][x] = t;
        squares[y][x].setIcon(ChooseIcon(s));
        set[y][x] = type.GetIndex(t.type);
    }

    private Board ReturnBoard() {
        return this;
    }

    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            for (int i = 0; i < h + 1; i++) {
                for (int j = 0; j < w + 1; j++) {
                    if (source == squares[i][j]) {
                        if (i == h) {
                            if (j == 0) {
                                new Popup("What should the savefile be called?").SavePopup(ReturnBoard());
                            } else {
                                GoToMenu();
                            }
                            return;
                        } else if (j < w) {
                            processClick(i, j);
                            return;
                        } else {
                            for (int k = 0; k < h; k++) {
                                if (k == i) {
                                    if (squares[k][j].getBackground() == Color.WHITE) {
                                        squares[k][j].setBackground(Color.YELLOW);
                                        switch (k) {
                                            case 0:
                                                now = type.GetIndex("1pipeLR");
                                                break;
                                            case 1:
                                                now = type.GetIndex("1turnLU");
                                                break;
                                            case 2:
                                                now = type.GetIndex("eraser");
                                                break;
                                            default:
                                                now = type.GetIndex("empty");
                                                break;
                                        }
                                    } else {
                                        squares[k][j].setBackground(Color.WHITE);
                                        now = type.GetIndex("empty");
                                        return;
                                    }
                                } else {
                                    squares[k][j].setBackground(Color.WHITE);
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private void updateSet(String s, int i, int j) {
        int n = type.GetIndex(s);
        set[i][j] = n;
        squares[i][j].setIcon(ChooseIcon(type.type[n]));
        tile[i][j] = new Tile(type.type[n]);
        tile[i][j].x = j;
        tile[i][j].y = i;
    }

    private void checkIfNull() {
        if (pipecount[0] == 0) {
            squares[0][w].setIcon(null);
        } else {
            squares[0][w].setIcon(ChooseIcon("1pipeLR"));
        }

        if (pipecount[1] == 0) {
            squares[1][w].setIcon(null);
        } else {
            squares[1][w].setIcon(ChooseIcon("1turnLU"));
        }
    }

    private void processClick(int i, int j) {
        if (type.type[now].equals("eraser") && tile[i][j].type.contains("1") == true) {
            if (tile[i][j].type.contains("pipe")) {
                pipecount[0]++;
            } else if (tile[i][j].type.contains("turn")) {
                pipecount[1]++;
            }
            updateSet("empty", i, j);
        }
        labels[0].setText(Character.toString(pipecount[0] + '0'));
        labels[1].setText(Character.toString(pipecount[1] + '0'));
        checkIfNull();

        if (tile[i][j].type.charAt(0) == '1' && now != type.GetIndex("eraser")) {
            Rotate(tile[i][j]);
        } else if (set[i][j] == type.GetIndex("empty") && now != type.GetIndex("empty")) {
            if (pipecount[0] > 0 && type.type[now].contains("pipe")) {
                pipecount[0]--;
                labels[0].setText(Character.toString(pipecount[0] + '0'));
            } else if (pipecount[1] > 0 && type.type[now].contains("turn")) {
                pipecount[1]--;
                labels[1].setText(Character.toString(pipecount[1] + '0'));
            } else {
                return;
            }
            updateSet(type.type[now], i, j);
        }
        checkIfNull();

        if (CheckIfFinished()) {
            timer.cancel();
            try {
                PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
                new File("src/player/PlayerData.txt").delete();
                if (player.getLevel() < nextLevel) {
                    player.setLevel(nextLevel);
                    player.addToScore(nextLevel*15/time);
                }
                PlayersRanking newrank = new PlayersRanking();
                for (Player hum : ranking) {
                    if (hum.getId() == player.getId())
                        newrank.addSort(player);
                    else
                        newrank.addSort(hum);
                }
                Saves.writePlayersToFile(newrank, "src/player/PlayerData.txt");
            } catch (IOException err) {
                return;
            }
            menu.LevelListGUI menu = new menu.LevelListGUI();
            menu.setVisible(true);
            menu.setAlwaysOnTop(false);
            menu.p = player;
            Popup tmp = new Popup("Congratulations! You have completed the level!");
            tmp.setVisible(true);
            this.dispose();
        }
    }
}
