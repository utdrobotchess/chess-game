package edu.utdallas.robotchess.manager;

import java.util.*;
import edu.utdallas.robotchess.game.*;

public class RobotDemoManager extends Manager
{
    public void handleSquareClick(int index)
    {
        if (currentlySelectedPiece == null)
            handleInitialPieceSelection(squareIndex);
        else
            handleMoveLocationSelection(squareIndex);
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
