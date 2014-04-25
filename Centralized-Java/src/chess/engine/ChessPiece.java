package chess.engine;

import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public abstract class ChessPiece {
    private int itsLocation;
    private Team itsTeam;
    private int itsNumberOfPriorMoves;
    
    protected int getLocation() {
        return itsLocation;
    }
    
    protected int getNumberOfPriorMoves() {
        return itsNumberOfPriorMoves;
    }
    
    protected abstract ArrayList<Integer> getPossibleMoveLocations();
    
    protected Team getTeam() {
        return itsTeam;
    }
    
    protected void setLocation(int newLocation) {
        itsLocation = newLocation;
    }
    
    protected void setNumberOfPriorMoves(int numberOfPriorMoves) {
        itsNumberOfPriorMoves = numberOfPriorMoves;
    }
    
    protected void setTeamFromInitialLocation(int initialLocation) {
        if(initialLocation < 32)
            itsTeam = Team.GREEN;
        else
            itsTeam = Team.ORANGE;
    }
    
    protected enum Team {
        ORANGE, GREEN
    }
}
