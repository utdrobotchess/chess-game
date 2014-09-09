package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class King extends ChessPiece{
    private static final String white = prefix + "WKing.png";
    private static final String black = prefix + "BKing.png";
    private static ImageIcon WKingPic = new ImageIcon(white);
    private static ImageIcon BKingPic = new ImageIcon(black);
    private static final int NUM_NEIGHBOR_DIRECTIONS = 8;
    protected static King spawnAt(Square location) {
        King k = new King();
        k.setTeamFromInitialLocation(location);
        k.setLocation(location);
        assignStringName(k);
        return k;
    }
    @Override
    public String toString() {
        return ID;
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        for (int i = 0; i < NUM_NEIGHBOR_DIRECTIONS; i++) {
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 1);
        }
        Square rightFirstSquare = getLocation();
        ChessPiece king = rightFirstSquare.getOccupant();
        
        if(king.hasNotMoved()){
            
            // Check if the king can castle right
            rightFirstSquare = rightFirstSquare.getNeighborInDirection(2);
            Square rightSecondSquare = rightFirstSquare.getNeighborInDirection(2);
            Square rookSquare = rightSecondSquare.getNeighborInDirection(2);
            if(!rightFirstSquare.isOccupied() && !rightSecondSquare.isOccupied()
                    && rookSquare.isOccupied() &&
                    rookSquare.getOccupant() instanceof Rook &&
                    rookSquare.getOccupant().hasNotMoved()){
                possibleMoveLocations.add(rightSecondSquare);
            }

            Square LeftFirstSquare = getLocation();
            
            // Check if the king can castle left
            LeftFirstSquare = LeftFirstSquare.getNeighborInDirection(6);
            Square LeftSecondSquare = LeftFirstSquare.getNeighborInDirection(6);
            Square LeftThirdSquare = LeftSecondSquare.getNeighborInDirection(6);
            Square LeftrookSquare = LeftThirdSquare.getNeighborInDirection(6);
            if(!LeftFirstSquare.isOccupied() && !LeftSecondSquare.isOccupied() && !LeftThirdSquare.isOccupied() 
                    && LeftrookSquare.isOccupied() &&
                    LeftrookSquare.getOccupant() instanceof Rook &&
                    LeftrookSquare.getOccupant().hasNotMoved()){
                possibleMoveLocations.add(LeftSecondSquare);
            }
        }
        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }
    public static void assignStringName(King k){
        if(k.getTeam() == Team.ORANGE){
            k.setImage(WKingPic);
            k.setName("A");
        }
        else{
            k.setImage(BKingPic);
            k.setName("a");
        }
    }
}
