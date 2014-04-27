package chess.engine;

import java.util.*;

/**
 * Contains the methods and attributes common to all chess pieces
 * @author Ryan J. Marcotte
 */
public abstract class ChessPiece {
    private Square itsLocation;
    private Team itsTeam;
    private int itsNumberOfPriorMoves;
    
    protected void addPossibleMoveLocationsInDirection(
            ArrayList<Square> possibleMoveLocations, int direction, int depth) {
        Square testSquare = itsLocation;
        
        for(int i = 0; i < depth; i++) {
            testSquare = testSquare.getNeighborInDirection(direction);
            
            if(testSquare instanceof PerimeterSquare)
                break;
            
            if(testSquare.isOccupied()) {
                ChessPiece occupant = testSquare.getOccupant();
                
                if(occupant.getTeam() == itsTeam) {
                    break;
                } else {
                    possibleMoveLocations.add(testSquare);
                    break;
                }
            } else {
                possibleMoveLocations.add(testSquare);
            }
            
        }
    }
    
    protected int getIntegerLocation() {
        return itsLocation.getLocation();
    }
    
    protected Square getLocation() {
        return itsLocation;
    }
    
    protected int getNumberOfPriorMoves() {
        return itsNumberOfPriorMoves;
    }
    
    protected abstract ArrayList<Square> getPossibleMoveLocations();
    
    protected Team getTeam() {
        return itsTeam;
    }
    
    protected void setLocation(Square newLocation) {
        itsLocation = newLocation;
        itsLocation.setOccupant(this);
    }
    
    protected void setNumberOfPriorMoves(int numberOfPriorMoves) {
        itsNumberOfPriorMoves = numberOfPriorMoves;
    }
    
    protected void setTeamFromInitialLocation(Square initialLocation) {
        if(initialLocation.getLocation() < 32)
            itsTeam = Team.GREEN;
        else
            itsTeam = Team.ORANGE;
    }
    
    protected enum Team {
        ORANGE(-1), GREEN(1);
        
        private final int directionalValue;
        private Team(int value) {
            directionalValue = value;
        }
        
        public int getDirectionalValue() {
            return directionalValue;
        }
    }
}
