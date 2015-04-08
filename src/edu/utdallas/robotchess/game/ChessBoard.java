package edu.utdallas.robotchess.game;

public class ChessBoard
{
    private final int NUM_SQUARES = 64;
    private final int NUM_COLUMNS = 8;
    private final int NUM_ROWS = 8;

    private final int NORTH = 0;
    private final int NORTHEAST = 1;
    private final int EAST = 2;
    private final int SOUTHEAST = 3;
    private final int SOUTH = 4;
    private final int SOUTHWEST = 5;
    private final int WEST = 6;
    private final int NORTHWEST = 7;


    Square squares[] = new Square[NUM_SQUARES];

    public ChessBoard()
    {
        createSquares();
        assignAllNeighbors();
    }

    private void createSquares()
    {
        for (int i = 0; i < NUM_SQUARES; i++)
            squares[i] = new Square(i);
    }

    private void assignAllNeighbors()
    {
        for (int i = 0; i < NUM_SQUARES; i++) {
            int neighborIndexes[] = new int[8];

            neighborIndexes[NORTH] = calculateNorthNeighborIndex(i);
            neighborIndexes[NORTHEAST] = calculateNortheastNeighborIndex(i);
            neighborIndexes[EAST] = calculateEastNeighborIndex(i);
            neighborIndexes[SOUTHEAST] = calculateSoutheastNeighborIndex(i);
            neighborIndexes[SOUTH] = calculateSouthNeighborIndex(i);
            neighborIndexes[SOUTHWEST] = calculateSouthwestNeighborIndex(i);
            neighborIndexes[WEST] = calculateWestNeighborIndex(i);
            neighborIndexes[NORTHWEST] = calculateNorthwestNeighborIndex(i);

            for (int j = 0; j < neighborIndexes.length; j++)
                assignNeighbor(i, j, neighborIndexes[j]);
        }
    }

    private int calculateNorthNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != 0)
            return squareIndex - NUM_COLUMNS;

        return -1;
    }

    private int calculateNortheastNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != 0 &&
            squareIndex % NUM_COLUMNS != (NUM_COLUMNS - 1))
            return squareIndex - NUM_COLUMNS + 1;

        return -1;
    }

    private int calculateEastNeighborIndex(int squareIndex)
    {
        if (squareIndex % NUM_COLUMNS != (NUM_COLUMNS - 1))
            return squareIndex + 1;

        return -1;
    }

    private int calculateSoutheastNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != (NUM_ROWS - 1) &&
            squareIndex % NUM_COLUMNS != (NUM_COLUMNS - 1))
            return squareIndex + NUM_COLUMNS + 1;

        return -1;
    }

    private int calculateSouthNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != (NUM_ROWS - 1))
            return squareIndex + NUM_COLUMNS;

        return -1;
    }

    private int calculateSouthwestNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != (NUM_ROWS - 1) &&
            squareIndex % NUM_COLUMNS != 0)
            return squareIndex + NUM_COLUMNS - 1;

        return -1;
    }

    private int calculateWestNeighborIndex(int squareIndex)
    {
        if (squareIndex % NUM_COLUMNS != 0)
            return squareIndex - 1;

        return -1;
    }

    private int calculateNorthwestNeighborIndex(int squareIndex)
    {
        if (squareIndex / NUM_ROWS != 0 &&
            squareIndex % NUM_COLUMNS != 0)
            return squareIndex - NUM_COLUMNS - 1;

        return -1;
    }

    private void assignNeighbor(int squareIndex, int direction, int neighborIndex)
    {
        if (neighborIndex != -1)
            squares[squareIndex].setNeighbor(squares[neighborIndex], direction);
        else
            squares[squareIndex].setNeighbor(null, direction);
    }

    protected Square[] getSquares()
    {
        return squares;
    }

    protected Square getSquareAt(int intLocation)
    {
        if (intLocation < 0 || intLocation > 63)
            return null;

        return squares[intLocation];
    }
}
