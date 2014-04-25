package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessBoard {
    private Square[] itsSquares = new Square[100];
    
    private ChessBoard() {
        
    }
    
    protected static ChessBoard generateChessBoard() {
        ChessBoard board = new ChessBoard();
        ChessBoardBuilder.build(board);
        return board;
    }
    
    protected Square getSquareAt(int location) {
        return itsSquares[location];
    }
    
    protected Square[] getAllSquares() {
        return itsSquares;
    }
}
