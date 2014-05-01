package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public class NullSquare extends Square {
    private NullSquare() {
        
    }
    
    protected static NullSquare generateNullSquare() {
        NullSquare nullSq = new NullSquare();
        nullSq.setOccupant(NullChessPiece.spawnAt(nullSq));
        nullSq.setLocation(-1);
        return nullSq;
    }
}
