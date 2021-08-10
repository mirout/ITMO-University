package game.player;

import game.*;

public class CheatingPlayer implements Player {

    @Override
    public Move move(Position position, Cell cell) {
        final Board board = (Board) position;
        Move validMove = new Move(0, 0, cell);

        for (int row = 0; row < position.getM(); row++){
            for (int col = 0; col < position.getN(); col++){
                if (row == col && col == 0) {
                    continue;
                }
                if (position.getCell(row, col) == Cell.EMPTY) {
                    Move move = new Move(row, col, cell);
                    board.makeMove(move);
                    cell = cell == Cell.X ? Cell.O : Cell.X;
                }
            }
        }
        return validMove;
    }

}
