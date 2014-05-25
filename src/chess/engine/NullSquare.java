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
        nullSq.setNumericalLocation(-1);
        nullSq.setOccupancy(false);
		nullSq.setOccupyingTeam(Team.NEUTRAL);
		return nullSq;
    }
}
