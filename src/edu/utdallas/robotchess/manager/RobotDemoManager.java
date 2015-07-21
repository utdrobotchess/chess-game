package edu.utdallas.robotchess.manager;

import java.util.ArrayList;

import edu.utdallas.robotchess.game.Square;
import edu.utdallas.robotchess.pathplanning.MotionPlanner;
import edu.utdallas.robotchess.pathplanning.Path;
import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.SmartCenterCommand;

public class RobotDemoManager extends Manager
{
    public RobotDemoManager(int[] initialPieceLocations)
    {
        super();
        game.initializePieces(initialPieceLocations);
    }

    public void handleSquareClick(int index)
    {
        if (currentlySelectedPiece != null &&
            index == currentlySelectedPiece.getIntLocation()) {
            comm.sendCommand(new SmartCenterCommand(currentlySelectedPiece.getID()));
            currentlySelectedPiece = null;
            return;
        }

        int[] currentLocations = game.getPieceLocations();
        super.handleSquareClick(index);
        int[] desiredLocations = game.getPieceLocations();

        MotionPlanner planner = new MotionPlanner(getBoardRowCount(),
                                                  getBoardColumnCount());
        ArrayList<Path> plan = planner.plan(currentLocations, desiredLocations);

        for (Path path : plan) {
            log.debug(path); //temp
            Command command = path.generateCommand();

            if (command == null)
                log.debug("Command is null. The pathplanner could not find a solution.");
            else
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

    public ArrayList<Integer> getValidMoveLocations()
    {
        return new ArrayList<>();
    }

    protected void makeUpdatesFromValidMoveSelection(int selectionIndex)
    {
        if (currentlySelectedPiece != null) {
            currentlySelectedPiece.moveTo(game.getBoardSquareAt(selectionIndex));
            currentlySelectedPiece = null;
        }
    }
}
