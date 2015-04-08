package edu.utdallas.robotchess.manager;

import java.util.*;

import edu.utdallas.robotchess.game.*;

public class NullManager extends Manager
{
    public NullManager()
    {
        super();
        int[] pieceLocations = {-1, -1, -1, -1, -1, -1, -1, -1,
                                -1, -1, -1, -1, -1, -1, -1, -1,
                                -1, -1, -1, -1, -1, -1, -1, -1,
                                -1, -1, -1, -1, -1, -1, -1, -1};

        game.initializePieces(pieceLocations);
    }

    protected boolean isValidInitialPieceSelection(int selectionIndex)
    {
        return false;
    }

    protected boolean isValidMoveLocationSelection(int selectionIndex)
    {
        return false;
    }

    protected void makeUpdatesFromValidMoveSelection(int selectionIndex)
    {

    }
}
