package game;

import game.player.HumanPlayer;
import game.player.RandomPlayer;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {

        ScannerIntegerReader sc = new ScannerIntegerReader(new Scanner(System.in));

        int countPlayers = inputCountOfSomething("players", 2, Cell.MAX_PLAYERS, sc);
        int countBots = inputCountOfSomething("bots", 0, countPlayers, sc);

        List<Player> players = new ArrayList<>();

        for (int i = 0; i < countPlayers - countBots; i++) {
            players.add(new HumanPlayer());
        }
        for (int i = 0; i < countBots; i++) {
            players.add(new RandomPlayer());
        }

        final Game game = new Game(true, countPlayers, players);

        int size, k;

        while (true) {
            try {

                System.out.print("Input size: ");
                size = sc.nextPositive();
                System.out.print("Input k: ");
                k = sc.nextPositive();

                if (size == -1 || k == -1) {
                    System.out.println("Size and k must be positive integer");
                    continue;
                }

                game.play(new RhombusBoard(size, k, countPlayers));

                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int inputCountOfSomething(String name, int lower, int upper, ScannerIntegerReader sc) {
        int result;
        while (true) {
            System.out.printf("Input count of %s: ", name);
            result = sc.nextNotNegative();
            if (result < lower || result > upper){
                System.out.printf("Count of %s must be an integer between %d and %d%n", name, lower, upper);
                continue;
            }
            break;
        }
        return result;
    }
}
