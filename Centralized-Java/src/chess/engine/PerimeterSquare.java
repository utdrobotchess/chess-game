package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public class PerimeterSquare extends Square {
    private PerimeterSquare() {
        
    }
    
    protected static PerimeterSquare generatePerimeterSquareAt(int location) {
        PerimeterSquare perimSq = new PerimeterSquare();
        perimSq.setLocation(location);
        return perimSq;
    }
}
