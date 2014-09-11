package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Bishop extends ChessPiece{
    private static final String white = prefix + "WBishop.png";
    private static final String black = prefix + "BBishop.png";
    private static ImageIcon WBishopPic = new ImageIcon(white);
    private static ImageIcon BBishopPic = new ImageIcon(black);
    private final static int BOARD_WIDTH = 8;
    private final static int NUM_NEIGHBOR_DIRECTIONS = 8;
    protected static Bishop spawnAt(Square location) {
        Bishop b = new Bishop();
        b.setTeamFromInitialLocation(location);
        b.setLocation(location);
        b.setInitialNumericalLocation();
        assignStringName(b);
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
     public static void assignStringName(Bishop b){
        if(b.getTeam() == Team.ORANGE){
            b.setImage(WBishopPic);
            b.setName("B");
        }
        else{
            b.setImage(BBishopPic);
            b.setName("b");
        }
    }
}
