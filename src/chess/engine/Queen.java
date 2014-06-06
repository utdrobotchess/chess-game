package chess.engine;

import java.util.*;
import java.util.logging.*;

/**
 * Defines the behavior and movement unique to a queen
 * @author Ryan J. Marcotte
 */
public class Queen extends ChessPiece {
    private static final Logger logger = ChessLogger.getInstance().logger;

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

        for(int i = 0; i < 8; i++)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 8);

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "Queen (" + getID() + ")";
    }
}
