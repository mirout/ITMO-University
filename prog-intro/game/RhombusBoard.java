package game;

public class RhombusBoard extends MNKBoard {

    RhombusBoard(int size, int k, int countPlayers) {

        super(size * 2 - 1, size * 2 - 1, k, countPlayers);

        size = size * 2 - 1;

        int half = size / 2;

        for (int i = 0; i < size; i++) {
            int di = Math.abs(i - half);
            for (int j = 0; j < size; j++) {
                if (di + Math.abs(j - half) > half) {
                    cells[i][j] = Cell.BORDER;
                    countEmpty--;
                }
            }
        }
    }
}
