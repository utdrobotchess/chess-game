package edu.utdallas.robotchess.game;

import java.util.Collections;
import java.util.ArrayList;

public class Bishop extends ChessPiece
{
    public Bishop(Square location, int id)
    {
        super(location, id);
    }

    public Bishop(int id, Team team, boolean active, boolean hasNotMoved)
    {
        super(id, team, active, hasNotMoved);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Bishop(getID(), getTeam(),
                                            isActive(), hasNotMoved());
        return copiedPiece;
    }

    public String getName()
    {
        return getTeam() + "-bishop";
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 1; i < 8; i += 2)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);

        return moveList;
    }
}
