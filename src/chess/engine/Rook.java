package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Rook extends ChessPiece{
    private static final String white = prefix + "WRook.png";
    private static final String black = prefix + "BRook.png";
    private static ImageIcon WRookPic = new ImageIcon(white);
    private static ImageIcon BRookPic = new ImageIcon(black);
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    private static final int BOARD_WIDTH = 8;
    protected static Rook spawnAt(Square location) {
        Rook r = new Rook();
        r.setTeamFromInitialLocation(location);
        r.setLocation(location);
        assignStringName(r);
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
     public static void assignStringName(Rook r){
        if(r.getTeam() == Team.ORANGE){
            r.setImage(WRookPic);
            r.setName("R");
        }
        else{
            r.setImage(BRookPic);
            r.setName("r");
        }
    }
}
