package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Rook extends ChessPiece{
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;
    protected static Rook spawnAt(Square location) {
        Rook r = new Rook();
        r.setTeamFromInitialLocation(location);
        r.setLocation(location);
        r.setInitialNumericalLocation();
        assignStringName(r, "R", "Rook.png");
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
        return ID;
    }
}
