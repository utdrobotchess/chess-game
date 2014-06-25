/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

public class InteriorSquare extends Square {
    private static final Logger logger = ChessLogger.getInstance().logger;

    private InteriorSquare() {
        super();
    }

    protected static InteriorSquare generateInteriorSquareAt(int location) {
        InteriorSquare interSq = new InteriorSquare();
        interSq.setNumericalLocation(location);
		interSq.setOccupancy(false);
		interSq.setOccupyingTeam(Team.NEUTRAL);

        logger.log(Level.FINE, "Interior square generated at {0}", location);

        return interSq;
    }
}
