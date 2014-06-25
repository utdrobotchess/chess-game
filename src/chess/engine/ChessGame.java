/*
 * XXX Very incomplete as of 6/25/14
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import chess.engine.Team;

public class ChessGame {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private ArrayList<ChessPiece> itsChessPieces = new ArrayList<>();
    private ChessBoard itsBoard;
    private Team itsActiveTeam;

    private ChessGame() { }

    public static ChessGame setupGame() {
        ChessGame game = new ChessGame();

        ChessGameBuilder.build(game);

        logger.log(Level.INFO, "New chess game set up");

        return game;
    }

    protected void addBuiltChessBoard(ChessBoard builtChessBoard) {
        itsBoard = builtChessBoard;
        logger.log(Level.FINE, "Chess board added to chess game");
    }

    protected void addChessPiece(ChessPiece piece) {
        itsChessPieces.add(piece);
        logger.log(Level.FINE, "{0} added to chess game", piece);
    }

    protected Team getActiveTeam() {
        return itsActiveTeam;
    }

    protected ChessBoard getBoard() {
        return itsBoard;
    }

    protected void makeMove(int origin, int destination) {
        validateMove(origin, destination);
        updatePositions(origin, destination);
        prepareForNextTurn();
    }

    private void validateMove(int origin, int destination) {

    }

    private void updatePositions(int origin, int destination) {

    }

    private void prepareForNextTurn() {
        ArrayList<Square> possibleMoveLocations;
        ChessPiece piece;

        toggleActiveTeam();

        for (int i = 0; i < itsChessPieces.size(); i++) {
            possibleMoveLocations = new ArrayList<>();
            piece = itsChessPieces.get(i);
            possibleMoveLocations = piece.generatePossibleMoveLocations();
            removeMovesThatResultInCheck(piece, possibleMoveLocations);
            piece.setPossibleMoveLocations(possibleMoveLocations);
        }
    }

    private void removeMovesThatResultInCheck(ChessPiece moveLocationsPiece,
                                              ArrayList<Square> possibleMoveLocations) {

	}

    protected void setActiveTeam(Team team) {
        itsActiveTeam = team;
        logger.log(Level.FINER, "Active team set to {0}", team);
    }

    protected void toggleActiveTeam() {
        if (itsActiveTeam == Team.GREEN) {
            itsActiveTeam = Team.ORANGE;
        } else {
            itsActiveTeam = Team.GREEN;
        }

        logger.log(Level.FINER, "Active team toggled to {0}", itsActiveTeam);
    }
}
