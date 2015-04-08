package edu.utdallas.robotchess.manager;

import java.util.*;
import edu.utdallas.robotchess.game.*;
import edu.utdallas.robotchess.robot.*;

public class RobotDemoManager extends Manager
{
    private ChessbotCommunicator comm;

    public RobotDemoManager(int[] initialPieceLocations)
    {
        super();
        game.initializePieces(initialPieceLocations);
        comm = ChessbotCommunicator.create();
    }

    public void handleSquareClick(int index)
    {
        int[] currentLocations = game.getPieceLocations();
        super.handleSquareClick(index);
        int[] desiredLocations = game.getPieceLocations();

        MotionPlanner planner = new MotionPlanner(getBoardRowCount(),
                                                  getBoardColumnCount());
        ArrayList<Path> plan = planner.plan(currentLocations, desiredLocations);

        for (int i = 0; i < plan.size(); i++) {
            Path path = plan.get(i);
            Command command = path.generateCommand();
            comm.sendCommand(command);
        }
    }

    protected boolean isValidInitialPieceSelection(int selectionIndex)
    {
        Square selectedSquare = game.getBoardSquareAt(selectionIndex);

        return selectedSquare.isOccupied();
    }

    protected boolean isValidMoveLocationSelection(int selectionIndex)
    {
        Square selectedSquare = game.getBoardSquareAt(selectionIndex);

        return !selectedSquare.isOccupied();
    }

    protected void makeUpdatesFromValidMoveSelection(int selectionIndex)
    {
        currentlySelectedPiece.moveTo(game.getBoardSquareAt(selectionIndex));
        currentlySelectedPiece = null;
    }
}
