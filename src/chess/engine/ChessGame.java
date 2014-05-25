package chess.engine;

import java.util.*;
import chess.engine.ChessPiece.Team;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessGame {
    private ArrayList<ChessPiece> chessPieces = new ArrayList<>();
    private ChessBoard board;
    private Team activeTeam;
    
    private ChessGame() {
        
    }
    
    public static ChessGame setupGame() {
        ChessGame game = new ChessGame();
        
 //       ChessGameBuilder.build(game);
        
        return game;
    }
    
    protected void addBuiltChessBoard(ChessBoard builtChessBoard) {
        board = builtChessBoard;
    }
    
    protected void addChessPiece(ChessPiece piece) {
        chessPieces.add(piece);
    }
    
    protected Team getActiveTeam() {
        return activeTeam;
    }
    
    protected ChessBoard getBoard() {
        return board;
    }
    
    protected void makeMove(int origin, int destination) throws InvalidMoveException {
        validateMove(origin, destination);
        updatePositions(origin, destination);
        prepareForNextTurn();
    }
    
    private void validateMove(int origin, int destination) throws InvalidMoveException {
        
    }
    
    private void updatePositions(int origin, int destination) {
        ChessPiece pieceBeingMoved = board.getSquareAt(origin).getOccupant();
        
        pieceBeingMoved.setLocation(board.getSquareAt(destination));
        
        if(board.getSquareAt(destination).isOccupied()) {
            ChessPiece capturedPiece = board.getSquareAt(destination).getOccupant();
            
            for(int i = 64; i < 100; i++) {
                Square possibleCapturedDestination = board.getSquareAt(i);
                if(!possibleCapturedDestination.isOccupied()) {
                    capturedPiece.setLocation(possibleCapturedDestination);
                    break;
                }
            }  
        }
    }
    
    private Square getNextAvailablePerimeterSquare() {
        for(int i = 64; i < 100; i++) {
            Square possibleAvailablePerimeterSquare = board.getSquareAt(i);
 //           if(!possibleCaptured)
				
        }

		return board.getSquareAt(64);
    }
    
    private void prepareForNextTurn() {
        toggleActiveTeam();
        
        for(int i = 0; i < chessPieces.size(); i++) {
            ArrayList<Square> possibleMoveLocations = new ArrayList<>();
            ChessPiece piece = chessPieces.get(i);
            possibleMoveLocations = piece.generatePossibleMoveLocations();
            removeMovesThatResultInCheck(piece, possibleMoveLocations);
            piece.setPossibleMoveLocations(possibleMoveLocations);
        }
    }
    
    private void removeMovesThatResultInCheck(ChessPiece moveLocationsPiece, ArrayList<Square> possibleMoveLocations) {
        Square moveLocationInQuestion;
        ChessPiece testPiece;
        ArrayList<Square> enemyMoveLocations;
        Square currentLocation = moveLocationsPiece.getLocation();
        
        
        for(int i = 0; i < possibleMoveLocations.size(); i++) {
            moveLocationInQuestion = possibleMoveLocations.get(i);
            moveLocationsPiece.setLocation(moveLocationInQuestion);
            
            for(int j = 0; j < chessPieces.size(); j++) {
                testPiece = chessPieces.get(j);
                
                if(testPiece.getTeam() != moveLocationsPiece.getTeam()) {
                    enemyMoveLocations = testPiece.generatePossibleMoveLocations();
                    
                    for(int k = 0; k < enemyMoveLocations.size(); k++) {
                        if(enemyMoveLocations.get(i).getOccupant() instanceof King)
                            possibleMoveLocations.remove(moveLocationInQuestion);
                    }
                }
            }
        }
        
        moveLocationsPiece.setLocation(currentLocation);
    }
    
    protected void setActiveTeam(Team team) {
        activeTeam = team;
    }
    
    protected void toggleActiveTeam() {
        if(activeTeam == Team.GREEN)
            activeTeam = Team.ORANGE;
        else
            activeTeam = Team.GREEN;
    }
}

class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}
