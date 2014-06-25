/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

public class King extends ChessPiece {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();

    private King() {
        super();
    }

    protected static King spawnAt(Square location) {
        King k = new King();
        k.setTeamFromInitialLocation(location);
		k.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {k, location.getNumericalLocation()});

		return k;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i++) {
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 1);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "King (" + getID() + ")";
    }
}
