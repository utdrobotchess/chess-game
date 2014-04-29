package chess.engine;

import java.util.*;

/**
 * Defines the behavior and movement unique to a knight
 * @author Ryan J. Marcotte
 */
public class Knight extends ChessPiece{
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();
    
    private Knight() {
        super();
    }
    
    protected static Knight spawnAt(Square location) {
        Knight k = new Knight();
        k.setLocation(location);
        k.setTeamFromInitialLocation(location);
        k.setNumberOfPriorMoves(0);
        return k;
    }
    
    @Override
    protected ArrayList<Square> getPossibleMoveLocations() {
        for(int i = 0; i < 8; i += 2)
            addPossibleMovesInLateralDirection(possibleMoveLocations, i);
        
        Collections.sort(possibleMoveLocations);
        
        return possibleMoveLocations;
    }
    
    /*
    * Knights make L-shaped movements. To find possible move locations, divide the
    * locations according to the direction of the long side of the "L", which is 
    * two squares long. We move two squares away from the current location, then
    * test the adjacent squares in both directions that are perpendicular to that
    * two-square lateral movement.
    */
    private void addPossibleMovesInLateralDirection(ArrayList<Square> possibleMoveLocations, int direction) {
        Square currentLocation = getLocation();
        Square lateralTwoLocation = currentLocation;
            
        for(int j = 0; j < 2; j++)
            lateralTwoLocation = lateralTwoLocation.getNeighborInDirection(direction);
        
        Square[] testSquares = {lateralTwoLocation.getNeighborInDirection((direction + 2) % 8),
                                lateralTwoLocation.getNeighborInDirection((direction + 6) % 8)};
            
        for(int j = 0; j < 2; j++) {
            //make sure that the lateral two movement did not take us off the board
            if(testSquares[j] instanceof PerimeterSquare)
                continue;
            
            if(testSquares[j].isOccupied()) {
                ChessPiece occupant = testSquares[j].getOccupant();
                
                if(occupant.getTeam() == getTeam())
                    continue;
            }
                
            possibleMoveLocations.add(testSquares[j]);
        }
    }
}
