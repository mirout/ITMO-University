package game;

import java.util.Arrays;

public class MNKBoard implements Board {

    protected final Cell[][] cells;
    private Cell turn;
    private final int k;
    private final int n;
    private final int m;
    protected int countEmpty;
    private final int countPlayers;

    private final Position position = new Position() {
        @Override
        public boolean isValid(Move move) {
            return isPartOfBoard(move.getRow(), move.getColumn())
                    && getCell(move.getRow(), move.getColumn()) == Cell.EMPTY
                    && move.getValue() == turn;
        }

        @Override
        public int getN() {
            return n;
        }

        @Override
        public int getM() {
            return m;
        }

        @Override
        public Cell getCell(int r, int c) {
            return cells[r][c];
        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();

            int countDigitInRow= (int) Math.log10(m + 1);
            int countDigitInColumn = (int) Math.log10(n + 1);

            sb.append(" ".repeat(countDigitInColumn + 2));

            for (int i = 1; i < m+1; i++) {
                sb.append(i);
                sb.append(" ".repeat(countDigitInRow - (int) Math.log10(i) + 1));
            }

            for (int i = 0; i < n; i++) {
                sb.append("\n");
                sb.append(i + 1);
                sb.append(" ".repeat(countDigitInColumn - (int) Math.log10(i+1) + 1));
                for (int j = 0; j < m; j++) {
                    sb.append(cells[i][j]);
                    sb.append(" ".repeat(countDigitInRow + 1));
                }
            }

            return sb.toString();
        }
    };

    MNKBoard(int m, int n, int k, int countPlayers) {

        if (m <= 0 || n <= 0 || k <= 0) {
            throw new IllegalArgumentException("Dimension must be positive");
        } else if (k > m && k > n) {
            throw new IllegalArgumentException("k must be less than dimension");
        }

        this.m = m;
        this.n = n;
        this.k = k;
        this.countPlayers = countPlayers;

        countEmpty = n * m;

        cells = new Cell[n][m];

        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.EMPTY);
        }

        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public Result makeMove(Move move) {

        if (!position.isValid(move)) {
            return Result.CHEAT;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        countEmpty--;

        if (isWin(move.getRow(), move.getColumn())) {
            return Result.WIN;
        } else if (countEmpty == 0) {
            return Result.DRAW;
        }

        turn = turn.next(countPlayers);

        return Result.UNKNOWN;

    }

    public enum Direction {
        LEFTUP(1, 1),
        RIGHTUP(1, -1),
        LEFT(1, 0),
        UP(0, 1);

        private final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }

    private boolean isWin(int r, int c) {
        for (Direction direction : Direction.values()) {
            if (countEqualsCellAtLine(r, c, direction.getX(), direction.getY()) >= k) {
                return true;
            }
        }

        return false;
    }

    private int countEqualsCellAtLine(int row, int column, int stepX, int stepY){
        return 1 + countEqualsCellAtHalfLine(row, column, stepX, stepY) + countEqualsCellAtHalfLine(row, column, -stepX, -stepY);
    }

    private boolean isPartOfBoard(int row, int column) {
        return 0 <= column && column < m
                && 0 <= row && row < n;
    }

    private int countEqualsCellAtHalfLine(int row, int column, int stepX, int stepY) {

        int count = 0;

        for(int i = 1; i < k; i++) {

            row += stepY;
            column += stepX;

            if (!isPartOfBoard(row, column) || cells[row][column] != turn) {
                break;
            }

            count++;
        }
        return count;
    }
}
