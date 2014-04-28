package chess.engine;

import java.util.*;

/**
 * Defines the behavior and movement unique to a bishop
 * @author Ryan J. Marcotte
 */
public class Bishop extends ChessPiece {
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();
    
    private Bishop() {
        super();
    }
    
    protected static Bishop spawnAt(Square location) {
        Bishop b = new Bishop();
        b.setLocation(location);
        b.setTeamFromInitialLocation(location);
        b.setNumberOfPriorMoves(0);
        return b;
    }
    
    @Override
    protected ArrayList<Square> getPossibleMoveLocations() {
        for(int i = 1; i < 8; i += 2)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 8);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
}
