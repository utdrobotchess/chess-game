package edu.utdallas.robotchess.manager;

import java.util.*;
import edu.utdallas.robotchess.game.*;
import edu.utdallas.robotchess.robot.*;

public class RobotDemoManager extends Manager
{
    public RobotDemoManager()
    {
        super();
        int[] pieceLocations = {-1, -1, -1, -1, -1, -1, -1, -1, 
                                -1, -1, -1, -1, -1, -1, -1, -1,  
                                -1, -1, -1, -1, -1, -1, -1, -1,    
                                -1, -1, -1, -1, -1, -1, -1, -1};

        game.initializePieces(pieceLocations);
    }

    public void handleSquareClick(int index)
    {
        int[] currentLocations = game.getPieceLocations();
        super.handleSquareClick(index);
        int[] desiredLocations = game.getPieceLocations();
        
        MotionPlanner planner = new MotionPlanner(getBoardRowCount(), 
                                                  getBoardColumnCount());
        ArrayList<Path> plan = planner.plan(currentLocations, desiredLocations);
        
        // for every path in plan, communicate to the robots
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
