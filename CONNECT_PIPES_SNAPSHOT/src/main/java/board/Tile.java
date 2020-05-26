package board;

import javax.swing.JButton;

public class Tile {
    int x, y;           //location of this tile
    String type;         //type of tile, int instead of enum so it can be loaded automatically
    boolean up = false, down = false, left = false, right = false;  //which sides this pipe connects to

    public Tile(String s) {
        type = s;
        CheckConnections();
    }
    
    private void CheckConnections() {
        int i = type.length();
        while (--i >= 0)
        switch (type.charAt(i)) {
            case 'L': left = true; break;
            case 'R': right = true; break;
            case 'D': down = true; break;
            case 'U': up = true; break;
            default: return;
        }
    }
    
    //checks if the given tile is connected to tile at location x,y
    public boolean IsConnectedTo(Tile t) {
        if (y == t.y + 1 && up == true && t.down == true
                || x == t.x - 1 && right == true && t.left == true
                || x == t.x + 1 && left == true && t.right == true
                || y == t.y - 1 && down == true && t.up == true)
            return true;
        else return false;
    }
}
