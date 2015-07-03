package edu.utdallas.robotchess.manager;

import java.util.ArrayList;

import edu.utdallas.robotchess.game.*;
import edu.utdallas.robotchess.robotcommunication.ChessbotCommunicator;

public abstract class Manager
{
    private int boardRowCount;
    private int boardColumnCount;

    protected ChessGame game;
    protected ChessPiece currentlySelectedPiece;
    protected ChessbotCommunicator comm;

    protected Manager()
    {
        boardRowCount = 8;
        boardColumnCount = 8;

        game = new ChessGame();
        currentlySelectedPiece = null;
    }

    public void handleSquareClick(int index)
    {
        if (isValidInitialPieceSelection(index))
            handleInitialPieceSelection(index);
        else
            handleMoveLocationSelection(index);
    }

    private void handleInitialPieceSelection(int selectionIndex)
    {
        if (isValidInitialPieceSelection(selectionIndex))
            makeUpdatesFromValidPieceSelection(selectionIndex);
        else
            currentlySelectedPiece = null;
    }

    protected abstract boolean isValidInitialPieceSelection(int selectionIndex);

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

    public abstract ArrayList<Integer> getValidMoveLocations();
    protected abstract boolean isValidMoveLocationSelection(int selectionIndex);
    protected abstract void makeUpdatesFromValidMoveSelection(int selectionIndex);

    public ChessGame getGame()
    {
        return game;
    }

    public ArrayList<ChessPiece> getActivePieces()
    {
        return game.getActivePieces();
    }

    public ChessPiece getCurrentlySelectedPiece()
    {
        return currentlySelectedPiece;
    }

    public int getBoardRowCount()
    {
        return boardRowCount;
    }

    public int getBoardColumnCount()
    {
        return boardColumnCount;
    }

    public void setBoardRowCount(int boardRowCount)
    {
        this.boardRowCount = boardRowCount;
    }

    public void setBoardColumnCount(int boardColumnCount)
    {
        this.boardColumnCount = boardColumnCount;
    }

    public boolean isXbeeConnected()
    {
        if(comm == null)
            return false;
        else
            return comm.isConnected();
    }

    public boolean connectToXbee()
    {
        if(comm == null)
        {
            comm = ChessbotCommunicator.create();
            return comm.initializeCommunication();
        }
        else if(comm.isConnected())
            return true;
        else
            return comm.initializeCommunication();
    }

    public boolean checkIfAllChessbotsAreConnected()
    {
        return (comm.returnNumberofConnectedChessbots() == 32);
    }
}
