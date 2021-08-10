package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public enum Cell {
    X("X"),
    O("O"),
    L("|"),
    C("_"),
    EMPTY("."),
    BORDER(" ");

    String onField;
    final static int MAX_PLAYERS = 4;

    Cell(String str) {
        onField = str;
    }

    public Cell next(int countPlayers) {
        if (MAX_PLAYERS < countPlayers) {
            throw new IllegalArgumentException("Not enough cells");
        }
        return Cell.values()[(ordinal() + 1) % countPlayers];
    }

    @Override
    public String toString() {
        return onField;
    }
}
