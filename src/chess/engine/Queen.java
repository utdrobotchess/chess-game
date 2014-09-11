package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Queen extends ChessPiece{
    private static final String white = prefix + "WQueen.png";
    private static final String black = prefix + "BQueen.png";
    private static ImageIcon WQueenPic = new ImageIcon(white);
    private static ImageIcon BQueenPic = new ImageIcon(black);
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;
    protected static Queen spawnAt(Square location) {
        Queen q = new Queen();
        q.setTeamFromInitialLocation(location);
        q.setLocation(location);
        q.setInitialNumericalLocation();
        assignStringName(q);
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
    public static void assignStringName(Queen q){
        if(q.getTeam() == Team.ORANGE){
            q.setImage(WQueenPic);
            q.setName("Q");
        }
        else{
            q.setImage(BQueenPic);
            q.setName("q");
        }
    }
}
