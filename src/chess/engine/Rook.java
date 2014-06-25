/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

public class Rook extends ChessPiece {
    private final static Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;

    private Rook() {
        super();
    }

    protected static Rook spawnAt(Square location) {
        Rook r = new Rook();
        r.setTeamFromInitialLocation(location);
		r.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {r, location.getNumericalLocation()});

        return r;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        /* Explore all of the even-numbered directions (horizontal and vertical) */
        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i += 2) {
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, BOARD_WIDTH);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "Rook (" + getID() + ")";
    }
}
