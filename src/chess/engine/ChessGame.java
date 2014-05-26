package chess.engine;

import java.util.*;
import chess.engine.Team;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessGame {
    private ArrayList<ChessPiece> chessPieces = new ArrayList<>();
    private ChessBoard board;
    private Team activeTeam;
    
    private ChessGame() { }
    
    public static ChessGame setupGame() {
        ChessGame game = new ChessGame();
        
        ChessGameBuilder.build(game);
        
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
