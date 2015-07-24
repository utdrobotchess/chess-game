package edu.utdallas.robotchess.game;

import java.util.ArrayList;
import java.util.Collections;

import edu.utdallas.robotchess.pathplanning.Path;

public class Knight extends ChessPiece
{
    public Knight(Square location, int id)
    {
        super(location, id);
    }

    public Knight(int id, Team team, boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Knight(getID(), getTeam(),
                                            isActive(), hasNotMoved());
        return copiedPiece;
    }

    public String getName()
    {
        return getTeam() + "-knight";
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();
        Square currentLocation = getLocation();

        for (int i = 0; i < 8; i += 2) {
            Square neighbor = currentLocation.getNeighbor(i);

            if (neighbor == null)
                continue;

            Square twoAwayNeighbor = neighbor.getNeighbor(i);

            if (twoAwayNeighbor == null)
                continue;

            Square potentialMove;

            for (int j = 2; j < 7; j += 4) {
                potentialMove = twoAwayNeighbor.getNeighbor((i + j) % 8);

                if (potentialMove == null)
                    continue;

                if (potentialMove.isOccupied()) {
                    ChessPiece occupant = potentialMove.getOccupant();
                    Team theirTeam = occupant.getTeam();
                    Team ourTeam = getTeam();

                    if (theirTeam == ourTeam)
                        continue;
                }

                moveList.add(potentialMove);
            }
        }

        Collections.sort(moveList);

        return moveList;
    }

    public Path[] generatePaths(int destination)
    {
        ArrayList<Square> moveList = generateMoveLocations();

        //Knight will have two available paths for any move it makes
        Path[] paths = new Path[2];

        boolean isDestinationPossibleMove = false;
        int id = getID();
        int currentLocation = getLocation().toInt();

        for (Square moveLocationSquare : moveList) {
            if (destination == moveLocationSquare.toInt()) {
                isDestinationPossibleMove = true;
                break;
            }
        }

        for (int i = 0; i < paths.length; i++)
            paths[i] = new Path(id, currentLocation);

        //This is a bit verbose, but instructive I think.
        if (isDestinationPossibleMove) {
            int[][] possibleDirectionSequences = new int[][] {
                {ChessBoard.EAST, ChessBoard.SOUTH, ChessBoard.SOUTH},
                {ChessBoard.SOUTH, ChessBoard.SOUTH, ChessBoard.EAST},

                {ChessBoard.WEST, ChessBoard.SOUTH, ChessBoard.SOUTH},
                {ChessBoard.SOUTH, ChessBoard.SOUTH, ChessBoard.WEST},

                {ChessBoard.EAST, ChessBoard.NORTH, ChessBoard.NORTH},
                {ChessBoard.NORTH, ChessBoard.NORTH, ChessBoard.EAST},

                {ChessBoard.WEST, ChessBoard.NORTH, ChessBoard.NORTH},
                {ChessBoard.NORTH, ChessBoard.NORTH, ChessBoard.WEST},

                {ChessBoard.EAST, ChessBoard.EAST, ChessBoard.NORTH},
                {ChessBoard.NORTH, ChessBoard.EAST, ChessBoard.EAST},

                {ChessBoard.EAST, ChessBoard.EAST, ChessBoard.SOUTH},
                {ChessBoard.SOUTH, ChessBoard.EAST, ChessBoard.EAST},

                {ChessBoard.WEST, ChessBoard.WEST, ChessBoard.SOUTH},
                {ChessBoard.SOUTH, ChessBoard.WEST, ChessBoard.WEST},

                {ChessBoard.WEST, ChessBoard.WEST, ChessBoard.NORTH},
                {ChessBoard.NORTH, ChessBoard.WEST, ChessBoard.WEST}
            };

            //Might be an elegant way to do this without hardcoding...
            int[] possibleBoardDisplacements = new int[] {17, 15, -15, -17,
                                                            -6, 10, 6, -10};
            int index = 0;

            for (int boardDisplacement : possibleBoardDisplacements) {
                if (destination == currentLocation + boardDisplacement) {
                    possibleDirectionSequences = new int[][] {
                        possibleDirectionSequences[index],
                        possibleDirectionSequences[index + 1]
                    };

                    break;
                }

                index += 2;
            }

            if (possibleDirectionSequences.length > 2)
                possibleDirectionSequences = new int[][] {{},{}};

            index = 0;

            for (int[] directionSequence : possibleDirectionSequences) {
                Square neighbor = getLocation();

                for (int direction : directionSequence) {
                    neighbor = neighbor.getNeighbor(direction);
                    paths[index].add(neighbor.toInt());
                }

                //Should ensure that index doesn't go out of bounds for paths[]
                index++;
            }

        }
        return paths;
    }
}
