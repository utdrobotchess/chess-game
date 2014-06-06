package chess.engine;

import java.util.*;
import java.util.logging.*;

/**
 * Defines the behavior and movement unique to a king
 * @author Ryan J. Marcotte
 */
public class King extends ChessPiece {
    private final static Logger logger = ChessLogger.getInstance().logger;
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

        for(int i = 0; i < 8; i++)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 1);

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "King (" + getID() + ")";
    }
}
