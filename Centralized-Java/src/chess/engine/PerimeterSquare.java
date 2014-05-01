package chess.engine;

/**
 * Makes up the 36 spaces that border and surround a normal chessboard
 * @author Ryan J. Marcotte
 */
public class PerimeterSquare extends Square {
    private PerimeterSquare() {
        super();
    }
    
    protected static PerimeterSquare generatePerimeterSquareAt(int location) {
        PerimeterSquare perimSq = new PerimeterSquare();
        perimSq.setLocation(location);
        perimSq.setOccupant(NullChessPiece.spawnAt(perimSq));
        return perimSq;
    }
}
