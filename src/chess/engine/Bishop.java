/*
 * Defines the behavior and movement unique to a bishop
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import java.io.*;

public class Bishop extends ChessPiece {
    private final static int BOARD_WIDTH = 8;
    private final static int NUM_NEIGHBOR_DIRECTIONS = 8;
    private final static Logger logger = ChessLogger.getInstance().logger;

    private Bishop() {
        super();
    }

    protected static Bishop spawnAt(Square location) {
        Bishop b = new Bishop();
        b.setTeamFromInitialLocation(location);
        b.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {b, location.getNumericalLocation()});

		return b;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        /* Explore all of the odd-numbered (diagonal) directions */
        for (int i = 1; i < NUM_NEIGHBOR_DIRECTIONS; i += 2) {
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, BOARD_WIDTH);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "Bishop (" + getID() + ")";
    }
}
