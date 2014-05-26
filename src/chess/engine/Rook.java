package chess.engine;

import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class Rook extends ChessPiece {   
    private Rook() {
        super();
    }
    
    protected static Rook spawnAt(Square location) {
        Rook r = new Rook();
        r.setTeamFromInitialLocation(location);
		r.setLocation(location);
        return r;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();
        
        for(int i = 0; i < 8; i += 2)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 8);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
}
