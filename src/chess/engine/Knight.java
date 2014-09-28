package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Knight extends ChessPiece{
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    protected static Knight spawnAt(Square location) {
        Knight k = new Knight();
        k.setTeamFromInitialLocation(location);
        k.setLocation(location);
        k.setInitialNumericalLocation();
        assignStringName(k, "K", "Knight.png");
        return k;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        // Even number neighbors
        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i += 2) {
            addPossibleMovesInLateralDirection(possibleMoveLocations, i);
        }

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }
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
            if (testSquares[j] instanceof PerimeterSquare || testSquares[j].getNumericalLocation() == -1) {
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
        return ID;
    }
}
