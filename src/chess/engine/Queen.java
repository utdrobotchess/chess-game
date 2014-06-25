/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

public class Queen extends ChessPiece {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;

    private Queen() {
        super();
    }

    protected static Queen spawnAt(Square location) {
        Queen q = new Queen();
        q.setTeamFromInitialLocation(location);
        q.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {q, location.getNumericalLocation()});

		return q;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i++) {
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, BOARD_WIDTH);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "Queen (" + getID() + ")";
    }
}
