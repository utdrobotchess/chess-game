package edu.utdallas.robotchess.game;

import java.util.ArrayList;
import java.util.Collections;

public class Queen extends ChessPiece
{
    public Queen(Square location)
    {
        super(location);
    }
    
    public Queen(Square location, int id)
    {
        super(location, id); 
    }

    public Queen(int id, Team team,
                 boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Queen(getID(), getTeam(),
                                           isActive(), hasNotMoved());
        return copiedPiece;
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);

        return moveList;
    }
}
