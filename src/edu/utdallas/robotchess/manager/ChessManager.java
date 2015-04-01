package edu.utdallas.robotchess.manager;

import java.util.*;

import edu.utdallas.robotchess.game.*;

public class ChessManager extends Manager
{
    private ChessGame game;
    private ChessPiece currentlySelectedPiece;

    public ChessManager()
    {
        game = new ChessGame();
        currentlySelectedPiece = null;
    }

    public void handleSquareClick(int squareIndex)
    {
        if (currentlySelectedPiece == null)
            handleInitialPieceSelection(squareIndex);
        else
            handleMoveLocationSelection(squareIndex);
    }

    private void handleInitialPieceSelection(int selectionIndex)
    {
        if (isValidInitialPieceSelection(selectionIndex))
            makeUpdatesFromValidPieceSelection(selectionIndex);
        else
            currentlySelectedPiece = null;
    }

    private boolean isValidInitialPieceSelection(int selectionIndex)
    {
        Square selectedSquare = game.getBoardSquareAt(selectionIndex);

        return selectedSquare.isOccupied() &&
            selectedSquare.getOccupyingTeam() == game.getActiveTeam();
    }

    private void makeUpdatesFromValidPieceSelection(int selectionIndex)
    {
        Square selectedSquare = game.getBoardSquareAt(selectionIndex);
        currentlySelectedPiece = selectedSquare.getOccupant();
    }

    private void handleMoveLocationSelection(int selectionIndex)
    {
        if (isValidMoveLocationSelection(selectionIndex))
            makeUpdatesFromValidMoveSelection(selectionIndex);
    }

    private boolean isValidMoveLocationSelection(int selectionIndex)
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

    private void makeUpdatesFromValidMoveSelection(int selectionIndex)
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
        if (game.getActiveTeam() == Team.WHITE)
            game.setActiveTeam(Team.BLACK);
        else
            game.setActiveTeam(Team.WHITE);
    }
}
