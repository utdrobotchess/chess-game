package edu.utdallas.robotchess.engine;

public class Move
{
    public int pieceID;
    public int origin;
    public int destination;

    public Move(int pieceID, int origin, int destination)
    {
        this.pieceID = pieceID;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString()
    {
        return "move piece " + pieceID + " from " + origin + " to " + destination;
    }
}
