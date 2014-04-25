package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public class InteriorSquare extends Square {
    private InteriorSquare() {
        
    }
    
    protected static InteriorSquare generateInteriorSquareAt(int location) {
        InteriorSquare interSq = new InteriorSquare();
        interSq.setLocation(location);
        return interSq;
    }
}
