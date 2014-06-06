package chess.engine;

import java.util.logging.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessBoard {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private Square[] itsSquares = new Square[100];

    private ChessBoard() { }

    protected static ChessBoard generateChessBoard() {
        ChessBoard board = new ChessBoard();
        ChessBoardBuilder.build(board);

        logger.log(Level.INFO, "Chess board generated");

        return board;
    }

    protected Square getSquareAt(int location) {
        return itsSquares[location];
    }

    protected Square[] getAllSquares() {
        return itsSquares;
    }
}
