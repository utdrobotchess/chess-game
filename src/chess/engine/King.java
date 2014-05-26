package chess.engine;

import java.util.*;

/**
 * Defines the behavior and movement unique to a king
 * @author Ryan J. Marcotte
 */
public class King extends ChessPiece {    
    private King() {
        super();
    }
    
    protected static King spawnAt(Square location) {
        King k = new King();
        k.setTeamFromInitialLocation(location);
		k.setLocation(location);
		return k;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();
        
        for(int i = 0; i < 8; i++)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 1);
        
//        addPossibleCastlingMoves(possibleMoveLocations);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
}
