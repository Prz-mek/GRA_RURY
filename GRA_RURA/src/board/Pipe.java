package board;



public class Pipe {
    byte[] pass = {0,0,0,0}; //czy rura przewodzi w {góra, lewo, dół, prawo}
    
    public void PType(int t) {
        switch(t) {
            case 0: case 1: break;
            case 2: pass[1] = 1; pass[3] = 1; break;
            case 3: pass[0] = 1; pass[2] = 1; break;
            case 4: pass[0] = 1; pass[3] = 1; break;
            case 5: pass[0] = 1; pass[1] = 1; break;
            case 6: pass[2] = 1; pass[1] = 1; break;
            case 7: pass[2] = 1; pass[3] = 1;
        }
    }
}
