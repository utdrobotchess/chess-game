/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.game;

import java.util.ArrayList;

public abstract class ChessPiece
{
    private Square location;
    private Team team;
    private int id;
    private boolean active;
    private boolean hasNotMoved;

    public ChessPiece(Square location)
    {
        setLocation(location);
        setTeamFromInitialLocation(location);
        active = true;
        hasNotMoved = true;
    }

    public ChessPiece(Square location, int id)
    {
        setLocation(location);
        this.id = id;
        setTeamFromInitialLocation(location);
        active = true;
        hasNotMoved = true;
    }

    public ChessPiece(Square location, int id, Team team,
                      boolean active, boolean hasNotMoved)
    {
        setLocation(location);
        this.id = id;
        this.team = team;
        this.active = active;
        this.hasNotMoved = hasNotMoved;
    }

    protected void addMovesInDirection(ArrayList<Square> moveList, 
                                       int direction, int limit)
    {
        Square neighbor = location.getNeighbor(direction);
        int squaresTraveled = 0;

        while (neighbor != null && squaresTraveled < limit) {
            if (neighbor.isOccupied()) {
                ChessPiece occupant = neighbor.getOccupant();
                Team theirTeam = occupant.getTeam();

                if (theirTeam != this.team)
                    moveList.add(neighbor);

                break;
            }

            moveList.add(neighbor);
            neighbor = neighbor.getNeighbor(direction);
            squaresTraveled++;
        }
    }

    protected abstract ArrayList<Square> generateMoveLocations();
    protected abstract ChessPiece copyPiece();

    protected int getID()
    {
        return id;
    }

    protected Square getLocation()
    {
        return location;
    }

    public Team getTeam()
    {
        return team;
    }

    public boolean isActive()
    {
        return active;
    }
    
    protected boolean hasNotMoved()
    {
        return hasNotMoved;
    }

    protected void moveTo(Square location)
    {
        hasNotMoved = false;

        if (location.isOccupied()) {
            ChessPiece occupant = location.getOccupant();
            occupant.setLocation(null);
            occupant.setActive(false);
        }
        
        this.location.setOccupant(null);
        setLocation(location);
        location.setOccupant(this);
    }

    protected void setActive(boolean active)
    {
        this.active = active;
    }
    
    protected void setHasNotMoved(boolean hasNotMoved)
    {
        this.hasNotMoved = hasNotMoved;
    }
    
    protected void setLocation(Square location)
    {
        this.location = location;

        if (location != null)
            location.setOccupant(this);
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