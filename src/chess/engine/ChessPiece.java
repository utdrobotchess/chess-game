package chess.engine;

import java.util.*;

/**
 * Contains the methods and attributes common to all chess pieces
 * @author Ryan J. Marcotte
 */
public abstract class ChessPiece implements Comparable<ChessPiece> {
    private Square itsLocation;
    private Team itsTeam;
    private int itsNumberOfPriorMoves = 0;
    private ArrayList<Square> itsPossibleMoveLocations;
   
	/*
	 * Explores a Squares in a particular direction up to a particular depth. Searches
	 * until it reaches the edge of the board, an occupied square, or the specified depth.
	 */	
    protected void addPossibleMoveLocationsInDirection(
            ArrayList<Square> possibleMoveLocations, int direction, int depth) {
        Square testSquare = itsLocation;
        
        for(int i = 0; i < depth; i++) {
            testSquare = testSquare.getNeighborInDirection(direction);
            
            if(testSquare instanceof PerimeterSquare)
                break; //search has reached the edge of the board
            
            if(testSquare.isOccupied()) {
                if(testSquare.getOccupyingTeam() == itsTeam) {
                    break; //cannot move through square occupied by teammate -- end search
                } else {
                    possibleMoveLocations.add(testSquare);
                    break; //can capture opponent -- add location, end search
                }
            } else {
                possibleMoveLocations.add(testSquare); //unoccupied interior square
            }
            
        }
    }
    
    protected int getNumericalLocation() {
        return itsLocation.getNumericalLocation();
    }
    
    protected Square getLocation() {
        return itsLocation;
    }
    
    protected ArrayList<Square> getPossibleMoveLocations() {
        return itsPossibleMoveLocations;
    }
    
    protected abstract ArrayList<Square> generatePossibleMoveLocations();
    
    protected Team getTeam() {
        return itsTeam;
    }
    
    protected boolean hasNotMoved() {
        return itsNumberOfPriorMoves == 0;
    }

	protected void incrementNumberOfPriorMoves() {
		itsNumberOfPriorMoves++;
	}
    
    protected void setLocation(Square newLocation) {
    	if(itsLocation != null) {
			//if it already occupied a square, reset the occupancy of that old square
			itsLocation.setOccupancy(false);
			itsLocation.setOccupyingTeam(Team.NEUTRAL);
		}

		itsLocation = newLocation;
		itsLocation.setOccupancy(true);
        itsLocation.setOccupyingTeam(itsTeam);
    }

    protected void setPossibleMoveLocations(ArrayList<Square> possibleMoveLocations) {
        itsPossibleMoveLocations = possibleMoveLocations;
    }
    
    protected void setTeamFromInitialLocation(Square initialLocation) {
		//if initial location is in top half of board (0-31), team is green
		if(initialLocation.getNumericalLocation() < 32)
            itsTeam = Team.GREEN;
        else //otherwise, team is orange
            itsTeam = Team.ORANGE;
    }
    
    @Override
    public int compareTo(ChessPiece secondPiece) {
        if(getNumericalLocation() < secondPiece.getNumericalLocation())
            return -1;
        else if (getNumericalLocation() == secondPiece.getNumericalLocation())
            return 0;
        else
            return 1;
    }
}
