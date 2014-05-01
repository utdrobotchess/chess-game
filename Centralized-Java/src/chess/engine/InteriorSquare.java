package chess.engine;

/**
 * Makes up the 64 squares of a normal chessboard
 * @author Ryan J. Marcotte
 */
public class InteriorSquare extends Square {
    private InteriorSquare() {
        super();
    }
    
    protected static InteriorSquare generateInteriorSquareAt(int location) {
        InteriorSquare interSq = new InteriorSquare();
        interSq.setLocation(location);
        interSq.setOccupant(NullChessPiece.spawnAt(interSq));
        return interSq;
    }
}
