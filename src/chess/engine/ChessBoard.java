package chess.engine;

/**
 *
 * @author Alexandre
 */
public class ChessBoard {
    private Square[] itsSquares = new Square[100];

    private ChessBoard() { }

    protected static ChessBoard generateChessBoard() {
        System.out.println("Making a chess board");
        ChessBoard board = new ChessBoard();
        ChessBoardBuilder.build(board);

       // logger.log(Level.INFO, "Chess board generated");

        return board;
    }

    public Square getSquareAt(int location) {
        return itsSquares[location];
    }

    protected Square[] getAllSquares() {
        return itsSquares;
    }
}
