package edu.utdallas.robotchess.game;

import java.util.Collections;
import java.util.ArrayList;

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
}
