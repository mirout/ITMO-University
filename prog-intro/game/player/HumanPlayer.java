package game.player;

import game.*;

import java.io.PrintStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer implements Player {
    private final PrintStream out;
    private final ScannerIntegerReader in;

    public HumanPlayer(final PrintStream out, final ScannerIntegerReader in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new ScannerIntegerReader());
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {

            out.println("Position: ");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");

            int row, column;

            row = in.nextPositive();
            column = in.nextPositive();

            if (row < 0 || column < 0) {
                System.out.println("Row and column must be positive integer");
                continue;
            }

            final Move move = new Move(row - 1, column - 1, cell);
            if (position.isValid(move)) {
                return move;
            }

            out.println("Move " + move + " is invalid");
        }
    }
}
