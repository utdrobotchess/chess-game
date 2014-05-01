package chess.engine;

import java.util.*;

/**
 * Defines the behavior and movement unique to a queen
 * @author Ryan J. Marcotte
 */
public class Queen extends ChessPiece {
    private Queen() {
        super();
    }
    
    protected static Queen spawnAt(Square location) {
        Queen q = new Queen();
        q.setLocation(location);
        q.setTeamFromInitialLocation(location);
        q.setNumberOfPriorMoves(0);
        return q;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();
        
        for(int i = 0; i < 8; i++)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 8);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
}
