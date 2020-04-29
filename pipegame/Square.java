package pipegame;


import javax.swing.ImageIcon;

public class Square {
    private int x;      // współrzędne?
    private int y;
    private ImageIcon fogeIcon;
    
    public Square() {
        x = 0;
        y = 0;
        fogeIcon = new ImageIcon("fogeicon.png");        // To nieznane pole (znak zapytania)
    }
    
    public Square(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public Square setX(int x) {
        this.x = x;
        return this;
    }
    
    public Square setY(int y) {
        this.y = y;
        return this;
    }
    
    public int getX() {
        return x;
    }
    
    public int setY() {
        return y;
    }
}
