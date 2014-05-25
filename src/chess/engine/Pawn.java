package chess.engine;

import java.util.*;

/**
 * Defines the behavior and movement unique to a pawn
 * @author Ryan J. Marcotte
 */
public class Pawn extends ChessPiece {
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();
    
    private Pawn() {
        super();
    }
   
    protected static Pawn spawnAt(Square location) {
        Pawn p = new Pawn();
        p.setTeamFromInitialLocation(location);
		p.setLocation(location);
        return p;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {        
        addPossibleForwardMoves();
        addPossibleCapturingMoves();
        
        //TODO removeLocationsThatResultInCheck(possibleMoveLocations);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
    
    private void addPossibleForwardMoves() {
        int possibleNumberOfForwardSquares = 1;
        
        if(hasNotMoved())
            possibleNumberOfForwardSquares = 2;
        
        addPossibleMoveLocationsInDirection(possibleMoveLocations, 
                determineForwardDirectionFromTeam(), possibleNumberOfForwardSquares);
    }
    
    private void addPossibleCapturingMoves() {
        Square currentLocation = getLocation();
        int forwardDirection = determineForwardDirectionFromTeam();
        
        Square[] possibleCaptureSquares = {currentLocation.getNeighborInDirection(forwardDirection + 1),
            currentLocation.getNeighborInDirection((forwardDirection + 7) % 8)};
        
        for(int i = 0; i < possibleCaptureSquares.length; i++) {
            Square testSquare = possibleCaptureSquares[i];
            
            if(testSquare.isOccupied()) {
                if(testSquare.getOccupyingTeam() != getTeam())
                    possibleMoveLocations.add(testSquare);
            }
        }
    }
    
    private int determineForwardDirectionFromTeam() {
        if(getTeam() == Team.GREEN)
            return 4;
        else
            return 0;
    }
}
