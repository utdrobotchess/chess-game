package chess.engine;

import java.util.*;
import java.util.logging.*;

/**
 * Defines the behavior and movement unique to a pawn
 * @author Ryan J. Marcotte
 */
public class Pawn extends ChessPiece {
    private final static Logger logger = ChessLogger.getInstance().logger;
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();

    private Pawn() {
        super();
    }

    protected static Pawn spawnAt(Square location) {
        Pawn p = new Pawn();
        p.setTeamFromInitialLocation(location);
		p.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {p, location.getNumericalLocation()});

        return p;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        addPossibleForwardMoves();
        addPossibleCapturingMoves();

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    private void addPossibleForwardMoves() {
        int possibleNumberOfForwardSquares = 1;

        if(hasNotMoved())
            possibleNumberOfForwardSquares = 2;

        addPossibleMoveLocationsInDirection(possibleMoveLocations,
                determineForwardDirectionFromTeam(), possibleNumberOfForwardSquares);
    }

    private void addPossibleCapturingMoves() {
        Square currentLocation = getLocation();
        int forwardDirection = determineForwardDirectionFromTeam();

        Square[] possibleCaptureSquares = {currentLocation.getNeighborInDirection(forwardDirection + 1),
            currentLocation.getNeighborInDirection((forwardDirection + 7) % 8)};

        for(int i = 0; i < possibleCaptureSquares.length; i++) {
            Square testSquare = possibleCaptureSquares[i];

            if(testSquare.isOccupied()) {
                if(testSquare.getOccupyingTeam() != getTeam())
                    possibleMoveLocations.add(testSquare);
            }
        }
    }

    private int determineForwardDirectionFromTeam() {
        if(getTeam() == Team.GREEN)
            return 4;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Pawn (" + getID() + ")";
    }
}
