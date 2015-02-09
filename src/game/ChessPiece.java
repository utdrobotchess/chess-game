/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;

public abstract class ChessPiece
{
    private Square location;
    private ArrayList<Square> possibleMoves;
    private Team team;
    private int id;
    private boolean active;

    public ChessPiece(Square location)
    {
        setLocation(location);
        possibleMoves = new ArrayList<>();
        setTeamFromInitialLocation(location);
        active = true;
    }

    public ChessPiece(Square location, int id)
    {
        setLocation(location);
        this.id = id;
        possibleMoves = new ArrayList<>();
        setTeamFromInitialLocation(location);
        active = true;
    }

    public ChessPiece(Square location, int id, Team team, boolean active)
    {
        setLocation(location);
        this.id = id;
        possibleMoves = new ArrayList<>();
        this.team = team;
        this.active = active;
    }

    protected void addMovesInDirection(ArrayList<Square> moveList, int direction, int limit)
    {
        Square neighbor = location.getNeighbor(direction);
        int squaresTraveled = 0;

        while (neighbor != null && squaresTraveled < limit) {
            if (neighbor.isOccupied()) {
                ChessPiece occupant = neighbor.getOccupant();
                Team theirTeam = occupant.getTeam();

                if (theirTeam != team)
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

    protected Team getTeam()
    {
        return team;
    }
    
    protected void setLocation(Square location)
    {
        this.location = location;
        location.setOccupant(this);
    }

    protected void setPossibleMoves(ArrayList<Square> possibleMoves)
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
