package chess.engine;

import java.util.*;
import java.util.logging.*;

/**
 * Defines the behavior and movement unique to a rook
 * @author Ryan J. Marcotte
 */
public class Rook extends ChessPiece {
    private final static Logger logger = ChessLogger.getInstance().logger;

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

        for(int i = 0; i < 8; i += 2)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 8);

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    @Override
    public String toString() {
        return "Rook (" + getID() + ")";
    }
}
