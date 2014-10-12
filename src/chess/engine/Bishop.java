package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Bishop extends ChessPiece{
    private final static int BOARD_WIDTH = 8;
    private final static int NUM_NEIGHBOR_DIRECTIONS = 8;
    protected static Bishop spawnAt(Square location) {
        Bishop b = new Bishop();
        b.setTeamFromInitialLocation(location);
        b.setLocation(location);
        b.setInitialNumericalLocation();
        assignStringName(b, "B", "Bishop.png");
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
        return ID;
    }
}
