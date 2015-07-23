package edu.utdallas.robotchess.game;

import java.util.ArrayList;
import java.util.Collections;

import edu.utdallas.robotchess.pathplanning.Path;

public class King extends ChessPiece
{
    public King(Square location, int id)
    {
        super(location, id);
    }

    public King(int id, Team team, boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new King(getID(), getTeam(),
                                          isActive(), hasNotMoved());
        return copiedPiece;
    }

    public String getName()
    {
        return getTeam() + "-king";
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            addMovesInDirection(moveList, i, 1);

        Collections.sort(moveList);

        return moveList;
    }

    //TODO: Implement castling
    public Path[] generatePaths(int destination)
    {
        ArrayList<Square> moveList = generateMoveLocations();

        //King will only have one path to take for any move
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
            int[] directions = new int[] {ChessBoard.NORTH,
                                        ChessBoard.NORTHEAST,
                                        ChessBoard.EAST,
                                        ChessBoard.SOUTHEAST,
                                        ChessBoard.SOUTH,
                                        ChessBoard.SOUTHWEST,
                                        ChessBoard.WEST,
                                        ChessBoard.NORTHWEST};

            Square thisSquare = getLocation();
            for (int direction : directions) {
                Square neighbor = thisSquare.getNeighbor(direction);

                if (neighbor == null)
                    continue;

                int neighborLocation = neighbor.toInt();

                if (neighborLocation == destination) {
                    path.add(neighborLocation);
                    break;
                }
            }

        }

        paths[0] = path;

        return paths;
    }
}
