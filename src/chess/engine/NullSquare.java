package chess.engine;

import java.util.logging.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class NullSquare extends Square {
    private static final Logger logger = ChessLogger.getInstance().logger;

    private NullSquare() {  }

    protected static NullSquare generateNullSquare() {
        NullSquare nullSq = new NullSquare();
        nullSq.setNumericalLocation(-1);
        nullSq.setOccupancy(false);
		nullSq.setOccupyingTeam(Team.NEUTRAL);

        logger.log(Level.FINE, "Null square generated");

		return nullSq;
    }
}
