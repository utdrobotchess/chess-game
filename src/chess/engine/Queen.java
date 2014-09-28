package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Queen extends ChessPiece{
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;
    protected static Queen spawnAt(Square location) {
        Queen q = new Queen();
        q.setTeamFromInitialLocation(location);
        q.setLocation(location);
        q.setInitialNumericalLocation();
        assignStringName(q, "Q", "Queen.png");
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
        return ID;
    }
}
