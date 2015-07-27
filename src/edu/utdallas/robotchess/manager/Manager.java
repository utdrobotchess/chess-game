package edu.utdallas.robotchess.manager;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.utdallas.robotchess.game.ChessGame;
import edu.utdallas.robotchess.game.ChessPiece;
import edu.utdallas.robotchess.game.Square;
import edu.utdallas.robotchess.robotcommunication.ChessbotCommunicator;
import edu.utdallas.robotchess.robotcommunication.ChessbotInfo;
import edu.utdallas.robotchess.robotcommunication.ChessbotInfoArrayHandler;
import edu.utdallas.robotchess.robotcommunication.RemoteController;
import edu.utdallas.robotchess.robotcommunication.commands.ReadLocationCommand;
import edu.utdallas.robotchess.robotcommunication.commands.SetLocationCommand;

public abstract class Manager
{
    private int boardRowCount;
    private int boardColumnCount;

    protected final static Logger log = Logger.getLogger(Manager.class);
    protected ChessGame game;
    protected ChessPiece currentlySelectedPiece;
    protected ChessbotCommunicator comm;
    protected RemoteController remoteController;

    protected Manager()
    {
        PropertyConfigurator.configure("log/log4j.properties");
        boardRowCount = 8;
        boardColumnCount = 8;

        game = new ChessGame();
        currentlySelectedPiece = null;
    }

    public boolean setInitialPieceLocations(int[] pieceLocations)
    {
        ChessGame g = new ChessGame();
        g.initializePieces(pieceLocations);
        if (g.isInCheckmate())
            return false;
        else
            this.game.initializePieces(pieceLocations);

        return true;
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

    public ChessbotCommunicator getComm() {
        return comm;
    }

    public void setComm(ChessbotCommunicator comm) {
        this.comm = comm;
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

    //Checking for comm == null is getting a bit messy. We may want to see if
    //we can reimplement the way we're using comm to clean this up.

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

    public ChessbotInfoArrayHandler getChessbotInfo()
    {
        if(comm == null)
            return null;
        else
            return comm.getChessbotInfo();
    }

    public void discoverChessbots()
    {
        if(comm == null)
            return;

        comm.discoverChessbots();
    }

    public boolean isDiscoveringChessbots()
    {
        if(comm == null)
            return false;

        return comm.isDiscoveringChessbots();
    }

    public void updateRobotLocations()
    {
        ChessbotInfoArrayHandler chessbotInfoArrayHandler = comm.getChessbotInfo();
        ArrayList<ChessbotInfo> chessbots = chessbotInfoArrayHandler.getChessbotInfoArrayList();

        for (ChessbotInfo chessbot : chessbots) {
            Integer botId = chessbot.getId();
    
            if (botId == null) {
                ReadLocationCommand cmd = new ReadLocationCommand(0);
                cmd.setXbeeAddress(chessbot.getXbeeAddress());
                comm.sendCommand(cmd);
            }
            else 
                comm.sendCommand(new ReadLocationCommand(chessbot.getId()));
        }
    }

    public void notifyRobotsOfTheirLocations()
    {
        int[] pieceLocations = game.getPieceLocations();
        byte botId = 0;

        //TODO: Update this so that it also checks if the botId exists within
        //the chessbots object of the ChessbotCommunicator class
        for (int pieceLocation : pieceLocations) {
            if (pieceLocation > -1)
                comm.sendCommand(new SetLocationCommand(botId, pieceLocation));

            botId++;
        }
    }

    public void createControllerThread(int botID)
    {
        remoteController = new RemoteController(comm, botID);
        remoteController.start();
    }

    public void destroyControllerThread()
    {
        remoteController.terminate();
    }
}
