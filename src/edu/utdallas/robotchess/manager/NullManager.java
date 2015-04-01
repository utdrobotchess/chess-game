package edu.utdallas.robotchess.manager;

import java.util.*;

import edu.utdallas.robotchess.game.*;

public class NullManager extends Manager
{
    public void handleSquareClick(int index)
    {

    }

    public int getBoardRowCount()
    {
        return 8;
    }

    public int getBoardColumnCount()
    {
        return 8;
    }

    public ArrayList<ChessPiece> getActivePieces()
    {
        return new ArrayList<>();
    }
}
