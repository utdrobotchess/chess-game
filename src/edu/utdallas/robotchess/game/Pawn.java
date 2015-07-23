package edu.utdallas.robotchess.game;

import java.util.ArrayList;
import java.util.Collections;

import edu.utdallas.robotchess.pathplanning.Path;

public class Pawn extends ChessPiece
{
    public Pawn(Square location, int id)
    {
        super(location, id);
    }

    public Pawn(int id, Team team, boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPawn = new Pawn(getID(), getTeam(),
                                         isActive(), hasNotMoved());
        return copiedPawn;
    }

    public String getName()
    {
        return getTeam() + "-pawn";
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        int neighborDirection;
        Square neighbor;
        Square currentLocation = getLocation();


        if (getTeam() == Team.ORANGE)
            neighborDirection = 0;
        else
            neighborDirection = 4;

        neighbor = currentLocation.getNeighbor(neighborDirection);

        // should we add square right in front of us?
        if (neighbor != null && !neighbor.isOccupied()) {
            moveList.add(neighbor);

            neighbor = neighbor.getNeighbor(neighborDirection);

            // should we add square two spaces in front of us?
            if (hasNotMoved() && neighbor != null && !neighbor.isOccupied())
                moveList.add(neighbor);
        }

        int captureDirections[] = new int[2];
        captureDirections[0] = (neighborDirection + 7) % 8;
        captureDirections[1] = (neighborDirection + 9) % 8;

        for (int i = 0; i < captureDirections.length; i++) {
            neighbor = currentLocation.getNeighbor(captureDirections[i]);

            if (neighbor != null && neighbor.isOccupied()) {
                ChessPiece occupant = neighbor.getOccupant();
                Team ourTeam = getTeam();
                Team theirTeam = occupant.getTeam();

                if (ourTeam != theirTeam)
                    moveList.add(neighbor);
            }
        }

        Collections.sort(moveList);

        return moveList;
    }

    public Path[] generatePaths(int destination)
    {
        ArrayList<Square> moveList = generateMoveLocations();

        //Pawn will only have one path to take for any move
        Path[] paths = new Path[1];

        boolean isDestinationPossibleMove = false;
        int id = getID();
        int currentLocation = getLocation().toInt();

        for (Square moveLocationSquare : moveList) {
            if (destination == moveLocationSquare.toInt()) {
                isDestinationPossibleMove = true;
                break;
            }
        }

        Path path = new Path(id, currentLocation);

        //May not need to check this...
        if (isDestinationPossibleMove) {
            int direction = -1;

            if (destination == currentLocation - 16)
                direction = ChessBoard.NORTH;
            else if (destination == currentLocation + 16)
                direction = ChessBoard.SOUTH;

            if (direction == -1)
                path.add(destination);
            else {
                Square firstSquare = getLocation().getNeighbor(direction);
                Square secondSquare = firstSquare.getNeighbor(direction);

                path.add(firstSquare.toInt());
                path.add(secondSquare.toInt());
            }

        }

        paths[0] = path;

        return paths;
    }
}
