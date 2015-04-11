package edu.utdallas.robotchess.manager;

import java.util.*;

import edu.utdallas.robotchess.game.*;

public class ChessManager extends Manager
{
    public ChessManager()
    {
        super();
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
        currentlySelectedPiece.moveTo(game.getBoardSquareAt(selectionIndex));

        if (isCastlingMove(selectionIndex))
            moveCastlingPiece(selectionIndex);

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

    private void toggleActiveTeam()
    {
        if (game.getActiveTeam() == Team.ORANGE)
            game.setActiveTeam(Team.GREEN);
        else
            game.setActiveTeam(Team.ORANGE);
    }
}
