package edu.utdallas.robotchess.manager;

import java.util.ArrayList;

import edu.utdallas.robotchess.game.ChessPiece;
import edu.utdallas.robotchess.game.King;
import edu.utdallas.robotchess.game.Square;
import edu.utdallas.robotchess.game.Team;
import edu.utdallas.robotchess.pathplanning.MotionPlanner;
import edu.utdallas.robotchess.pathplanning.Path;
import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.SmartCenterCommand;

//TODO: Clean up manager classes
public class RobotChessManager extends ChessManager
{
    private boolean computerControlsOrange = false;
    private boolean computerControlsGreen = false;
    private Path[] currentPiecePaths = null;

    public RobotChessManager()
    {
        super();
    }

    @Override
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

        handleRobotMovement(currentLocations, desiredLocations);
    }

    //TODO: need to simplify how I'm using currentPiecePaths
    private void handleRobotMovement(int[] currentLocations, int[] desiredLocations)
    {
        ArrayList<Path> plan = new ArrayList<>();

        if (currentPiecePaths != null) {
            MotionPlanner planner = new MotionPlanner(getBoardRowCount(),
                                                    getBoardColumnCount());

            plan = planner.plan(currentLocations, desiredLocations, currentPiecePaths);

            for (Path path : plan) {
                Command command = path.generateCommand();

                if (command == null)
                    log.debug("Command is null. The pathplanner could not find a solution.");
                else
                    comm.sendCommand(command);
            }

            currentPiecePaths = null;
        }

    }

    protected boolean isValidInitialPieceSelection(int selectionIndex)
    {
        Square selectedSquare = game.getBoardSquareAt(selectionIndex);

        return selectedSquare.isOccupied() &&
            selectedSquare.getOccupyingTeam() == game.getActiveTeam();
    }

    protected boolean isValidMoveLocationSelection(int selectionIndex)
    {
        if (currentlySelectedPiece == null)
            return false;

        ArrayList<Square> possibleMoveLocations =
            game.generateMoveLocations(currentlySelectedPiece);

        for (int i = 0; i < possibleMoveLocations.size(); i++)
            if (possibleMoveLocations.get(i).toInt() == selectionIndex)
                return true;

        return false;
    }

    public ArrayList<Integer> getValidMoveLocations()
    {
        ArrayList<Square> moveSquares = game.generateMoveLocations(currentlySelectedPiece);
        ArrayList<Integer> moveIndexes = new ArrayList<Integer>();

        for (int i = 0; i < moveSquares.size(); i++)
            moveIndexes.add(moveSquares.get(i).toInt());

        return moveIndexes;
    }

    protected void makeUpdatesFromValidMoveSelection(int selectionIndex)
    {
        if (isCastlingMove(selectionIndex))
            moveCastlingPiece(selectionIndex);

        currentPiecePaths = currentlySelectedPiece.generatePaths(selectionIndex);

        currentlySelectedPiece.moveTo(game.getBoardSquareAt(selectionIndex));
        currentlySelectedPiece = null;

        toggleActiveTeam();
    }

    private boolean isCastlingMove(int selectionIndex)
    {
        if (!(currentlySelectedPiece instanceof King))
            return false;

        Square selectedPieceSquare = currentlySelectedPiece.getLocation();
        for (int i = 0; i < 8; i++) {
            Square neighbor = selectedPieceSquare.getNeighbor(i);
            if (neighbor != null && neighbor.toInt() == selectionIndex)
                return false;
        }

        return true;
    }

    private void moveCastlingPiece(int selectionIndex)
    {
        int rookOriginIndex = -1;
        int rookDestinationIndex = -1;

        if (selectionIndex == 6 || selectionIndex == 62) {
            rookOriginIndex = selectionIndex + 1;
            rookDestinationIndex = selectionIndex - 1;
        } else {
            rookOriginIndex = selectionIndex - 2;
            rookDestinationIndex = selectionIndex + 3;
        }

        Square rookOriginSquare = game.getBoardSquareAt(rookOriginIndex);
        Square rookDestinationSquare = game.getBoardSquareAt(rookDestinationIndex);
        ChessPiece rook = rookOriginSquare.getOccupant();
        rook.moveTo(rookDestinationSquare);
    }

    public boolean isActiveTeamComputerControlled()
    {
        Team activeTeam = game.getActiveTeam();

        if (activeTeam == Team.ORANGE && computerControlsOrange ||
            activeTeam == Team.GREEN && computerControlsGreen)
            return true;

        return false;
    }

    public void setComputerControlsTeam(boolean computerControls, Team team)
    {
        if (team == Team.ORANGE)
            computerControlsOrange = computerControls;
        else
            computerControlsGreen = computerControls;
    }

    private void toggleActiveTeam()
    {
        game.toggleActiveTeam();
    }
}
