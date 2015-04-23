package edu.utdallas.robotchess.game;

public class Square implements Comparable<Square>
{
    final int NUM_NEIGHBORS = 8;
    int intLocation;
    Square neighbors[] = new Square[NUM_NEIGHBORS];
    ChessPiece occupant;

    public Square(int intLocation)
    {
        this.intLocation = intLocation;
        occupant = null;
    }

    protected Square copySquare()
    {
        Square copiedSquare = new Square(intLocation);
        copiedSquare.setOccupant(occupant);
        return copiedSquare;
    }

    @Override
    public int compareTo(Square otherSquare)
    {
        int otherIntLocation = otherSquare.toInt();

        if (intLocation < otherIntLocation)
            return -1;
        else if (intLocation == otherIntLocation)
            return 0;
        else
            return 1;
    }

    public Square getNeighbor(int direction)
    {
        return neighbors[direction];
    }

    public ChessPiece getOccupant()
    {
        return occupant;
    }

    public Team getOccupyingTeam()
    {
        return occupant.getTeam();
    }

    public int toInt()
    {
        return intLocation;
    }

    public boolean isOccupied()
    {
        return occupant != null;
    }

    protected void setNeighbor(Square neighbor, int direction)
    {
        neighbors[direction] = neighbor;
    }

    protected void setOccupant(ChessPiece occupant)
    {
        this.occupant = occupant;
    }

    @Override
    public String toString()
    {
        return "Square at " + intLocation;
    }
}
