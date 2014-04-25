package chess.engine;

import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class Pawn extends ChessPiece {
    private Pawn() {
        super();
    }
    
    protected static Pawn spawnPawnAt(int location) {
        Pawn p = new Pawn();
        p.setLocation(location);
        p.setTeamFromInitialLocation(location);
        p.setNumberOfPriorMoves(0);
        return p;
    }
    
    @Override
    protected ArrayList<Integer> getPossibleMoveLocations() {
        ArrayList<Integer> possibleMoveLocations = new ArrayList<>();
        
        if(getNumberOfPriorMoves() == 0) {
            possibleMoveLocations = getPossibleOpeningMoves();
        }
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
    
    private ArrayList<Integer> getPossibleOpeningMoves() {
        ArrayList<Integer> possibleOpeningMoves = new ArrayList<>();
        
        if(getTeam() == ChessPiece.Team.GREEN) {
            possibleOpeningMoves.add(getLocation() + 8);
            possibleOpeningMoves.add(getLocation() + 16);
        } else {
            possibleOpeningMoves.add(getLocation() - 8);
            possibleOpeningMoves.add(getLocation() - 16);
        }
        
        return possibleOpeningMoves;
    }
}
