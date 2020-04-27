package pipegame;

import java.io.IOException;

public class PipeGame {

    public static void main(String[] args) {
        Player p1 = new Player("Przemek", 0);
        Player p2 = new Player("Piotrek", 10);
        Player p3 = new Player("Dominik", 20);
        PlayersRanking ranking = new PlayersRanking();

        ranking.addSort(p2);
        ranking.addSort(p1);
        ranking.addSort(p3);

        for (Player p : ranking) {
            System.out.println(p);
        }
        try {
            Saves.writePlayersToFile(ranking, "test.txt");
        } catch (IOException e1) {
            System.err.println("Błąd przy zapisie do pliku");
        }

        PlayersRanking ranking2;
        try {
            ranking2 = Saves.readPlayersFromFile("test.txt");
            for (Player p : ranking2) {
                System.out.println(p);
            }
        } catch (IOException e2) {
            System.err.println("Błąd przy wczytywaniu z pliku");
        }
    }

}
