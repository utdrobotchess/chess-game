/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

public class Knight extends ChessPiece{
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;

    private Knight() {
        super();
    }

    protected static Knight spawnAt(Square location) {
        Knight k = new Knight();
        k.setTeamFromInitialLocation(location);
		k.setLocation(location);

        logger.log(Level.FINE, "{0} spawned at location {1}",
                   new Object[] {k, location.getNumericalLocation()});

        return k;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i += 2) {
            addPossibleMovesInLateralDirection(possibleMoveLocations, i);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

   /*
    * Knights make L-shaped movements. To find possible move locations, divide the
    * locations according to the direction of the long side of the "L", which is
    * two squares long. We move two squares away from the current location, then
    * test the adjacent squares in both directions that are perpendicular to that
    * two-square lateral movement.
    */
    private void addPossibleMovesInLateralDirection(ArrayList<Square> possibleMoveLocations,
                                                    int direction) {
        ChessPiece occupant;
        Square currentLocation = getLocation();
        Square lateralTwoLocation = currentLocation;

        for (int j = 0; j < 2; j++) {
            lateralTwoLocation = lateralTwoLocation.getNeighborInDirection(direction);
        }

        Square[] testSquares = {lateralTwoLocation.getNeighborInDirection((direction + 2) % 8),
                                lateralTwoLocation.getNeighborInDirection((direction + 6) % 8)};


        for (int j = 0; j < testSquares.length; j++) {
            /* make sure that the lateral two movement did not take us off the board */
            if (testSquares[j] instanceof PerimeterSquare) {
                continue;
            }

            /* make sure the square is not already occupied by our team */
            occupant = testSquares[j].getOccupant();
            if (occupant.getTeam() == getTeam()) {
	            continue;
            }

            possibleMoveLocations.add(testSquares[j]);
        }
    }

    @Override
    public String toString() {
        return "Knight (" + getID() + ")";
    }
}
