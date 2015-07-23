package edu.utdallas.robotchess.game;

import java.util.ArrayList;

import edu.utdallas.robotchess.pathplanning.Path;

public abstract class ChessPiece
{
    private Square location;
    private Team team;
    private boolean active;
    private int id;
    private boolean hasNotMoved;

    public ChessPiece(Square location)
    {
        setLocation(location);
        this.id = -1;
        setTeamFromInitialLocation(location.toInt());

        if (location != null)
            active = true;

        hasNotMoved = true;
    }

    public ChessPiece(Square location, int id)
    {
        setLocation(location);
        this.id = id;
        setTeamFromID();

        if (location != null)
            active = true;

        hasNotMoved = true;
    }

    public ChessPiece(int id, Team team, boolean active, boolean hasNotMoved)
    {
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
    public abstract Path[] generatePaths(int destination);
    public abstract String getName();

    public int getID()
    {
        return id;
    }

    public Square getLocation()
    {
        return location;
    }

    public int getIntLocation()
    {
        if (location != null)
            return location.toInt();

        return -1;
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

    public void moveTo(Square location)
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

    protected void setTeamFromID()
    {
        final int NUM_IDS = 32;

        if (id < NUM_IDS / 2)
            team = Team.GREEN;
        else
            team = Team.ORANGE;
    }

    protected void setTeamFromInitialLocation(int initialLocation)
    {
        final int NUM_SQUARES = 64;

        if (initialLocation < NUM_SQUARES / 2)
            team = Team.GREEN;
        else
            team = Team.ORANGE;
    }

    @Override
    public String toString()
    {
        int intLocation = -1;
        if (location != null)
            intLocation = location.toInt();

        return "Piece at " + intLocation;
    }
}
