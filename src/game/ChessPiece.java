/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;

public abstract class ChessPiece
{
    private Square location;
    private ArrayList<Integer> possibleMoves;
    private Team team;

    protected Square getLocation()
    {
        return location;
    }

    protected Team getTeam()
    {
        return team;
    }
    
    protected void setLocation(Square location)
    {
        this.location = location;
        location.setOccupant(this);
    }

    protected void setPossibleMoves(ArrayList<Integer> possibleMoves)
    {
        this.possibleMoves = possibleMoves;
    }

    protected void setTeamFromInitialLocation(Square initialLocation)
    {
        final int NUM_SQUARES = 64;

        if (initialLocation.getIntLocation() < NUM_SQUARES / 2)
            team = Team.BLACK;
        else
            team = Team.WHITE;
    }
}
