package edu.utdallas.robotchess.game;

import java.util.ArrayList;
import java.util.Collections;

import edu.utdallas.robotchess.pathplanning.Path;

public class Rook extends ChessPiece
{
    public Rook(Square location, int id)
    {
        super(location, id);
    }

    public Rook(int id, Team team, boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Rook(getID(), getTeam(),
                                          isActive(), hasNotMoved());
        return copiedPiece;
    }

    public String getName()
    {
        return getTeam() + "-rook";
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 7; i += 2)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);

        return moveList;
    }

    //TODO: Implement castling
    public Path[] generatePaths(int destination)
    {
        ArrayList<Square> moveList = generateMoveLocations();

        //Rook will only have one path to take for any move
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

        if (isDestinationPossibleMove) {
            int direction;

            if (destination > currentLocation &&
                destination % ChessBoard.NUM_COLUMNS > currentLocation % ChessBoard.NUM_COLUMNS)
                direction = ChessBoard.EAST;

            else if (destination > currentLocation &&
                destination % ChessBoard.NUM_COLUMNS == currentLocation % ChessBoard.NUM_COLUMNS)
                direction = ChessBoard.SOUTH;

            else if (destination < currentLocation &&
                destination % ChessBoard.NUM_COLUMNS < currentLocation % ChessBoard.NUM_COLUMNS)
                direction = ChessBoard.WEST;

            else
                direction = ChessBoard.NORTH;


            Square neighbor = getLocation();
            int neighborLocation = neighbor.toInt();

            while(neighborLocation != destination) {
                neighbor = neighbor.getNeighbor(direction);

                if (neighbor == null)
                    break;

                neighborLocation = neighbor.toInt();
                path.add(neighborLocation);
            }
        }

        paths[0] = path;

        return paths;
    }
}
