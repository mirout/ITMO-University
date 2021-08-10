package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Game {
    private final boolean log;
    private final int countPlayers;
    private final List<Player> players;

    public Game(final boolean log, final int countPlayers, final List<Player> players) {
        this.log = log;
        this.countPlayers = countPlayers;
        this.players = players;
    }

    public Result play(Board board) {
        while (true) {
            for (int i = 0; i < countPlayers; i++) {
                final Result result = move(board, players.get(i), i + 1);
                if (result != Result.UNKNOWN) {
                    return result;
                }
            }
        }
    }

    private Result move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getTurn());
        final Result result = board.makeMove(move);

        result.setWho(no);

        log("Player " + no + " move: " + move);
        log("Position:\n" + board.getPosition());

        if (result == Result.WIN) {
            log("Player " + no + " won");
            return result;
        } else if (result == Result.CHEAT) {
            log("Player " + no + " cheat");
            return result;
        } else if (result == Result.DRAW) {
            log("Draw");
            return result;
        } else {
            return result;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
