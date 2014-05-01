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
        k.setLocation(location);
        k.setTeamFromInitialLocation(location);
        k.setNumberOfPriorMoves(0);
        return k;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();
        
        for(int i = 0; i < 8; i++)
            addPossibleMoveLocationsInDirection(possibleMoveLocations, i, 1);
        
        addPossibleCastlingMoves(possibleMoveLocations);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
    
    private void addPossibleCastlingMoves(ArrayList<Square> possibleMoveLocations) {
        if(hasNotMoved()) {
            addPossibleEastCastling(possibleMoveLocations);
            addPossibleWestCastling(possibleMoveLocations);
        }
    }
    
    /*
    * In the case of green team, looks for castling with rook in square 7
    * In the case of orange team, looks for castling with rook in square 63
    */
    private void addPossibleEastCastling(ArrayList<Square> possibleMoveLocations) {
        Square testSquare = getLocation();
        ChessPiece testPiece;
        
        for(int i = 0; i < 3; i++) {
            testSquare = testSquare.getNeighborInDirection(2);
            
            //if there is an occupant in a square between the rook and knight, 
            //castling is not possible
            if(i < 2 && testSquare.isOccupied())
                return; 
        }
        
        testPiece = testSquare.getOccupant();
        
        if(testPiece instanceof Rook && testPiece.hasNotMoved())
            possibleMoveLocations.add(testSquare.getNeighborInDirection(6));
    }
    
    private void addPossibleWestCastling(ArrayList<Square> possibleMoveLocations) {
        Square testSquare = getLocation();
        ChessPiece testPiece;
        
        for(int i = 0; i < 4; i++) {
            testSquare = testSquare.getNeighborInDirection(6);
            
            //if there is an occupant in a square between the rook and knight, 
            //castling is not possible
            if(i < 3 && testSquare.isOccupied())
                return;
        }
        
        testPiece = testSquare.getOccupant();
        
        if(testPiece instanceof Rook && testPiece.hasNotMoved())
            possibleMoveLocations.add(testSquare.getNeighborInDirection(2));
    }
}
