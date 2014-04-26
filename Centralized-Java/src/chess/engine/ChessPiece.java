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
    
    protected int getIntegerLocation() {
        return itsLocation.getLocation();
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
        ORANGE, GREEN
    }
}
