package edu.utdallas.robotchess.manager;

import java.util.*;
import edu.utdallas.robotchess.game.*;

public class RobotDemoManager extends Manager
{
    private ChessPiece currentlySelectedPiece;

    public RobotDemoManager()
    {
        
    }

    public void handleSquareClick(int index)
    {
        if (currentlySelectedPiece == null)
            handleInitialPieceSelection(index);
        else
            handleMoveLocationSelection(index);
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
    
    private void handleInitialPieceSelection(int index)
    {

    }
    
    private void handleMoveLocationSelection(int index)
    {

    }
}
