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
    private GameState itsState;

    private ChessGame() { }

    protected static ChessGame setupGame() {
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

    protected ChessBoard getBoard() {
        return itsBoard;
    }

    protected GameState getState() {
        return itsState;
    }

    protected void setState(GameState state) {
        itsState = state;
    }


    public GameState updateStateFromUserSelection(int selectionLocation) {
        Square selectionSquare = itsBoard.getSquareAt(selectionLocation);
        ChessPiece occupant = selectionSquare.getOccupant();
        ArrayList<Square> possibleMoveLocations = new ArrayList<>();

        itsState.clearMovePairs();

        updatePossibleMoveLocations();

        if (occupant.getTeam() == itsState.getActiveTeam()) {
            itsState.setSelectedPieceIndex(selectionLocation);

            possibleMoveLocations = occupant.getPossibleMoveLocations();
            //if piece is king, check for castling
            //remove moves that result in check

            itsState.setPossibleMoveIndexes(possibleMoveLocations);
        } else if (itsState.getSelectedPieceIndex() != -1) {
            if (itsState.getPossibleMoveIndexes().contains(selectionLocation)) {
                ArrayList<Integer> movePairs = new ArrayList<>();
                movePairs = addMovePairs(selectionLocation);

                //check for en passant
                //check for castling
                itsState.setMovePairs(movePairs);

                for (int i = 0; i < movePairs.size(); i += 2) {
                    movePiece(movePairs.get(i), movePairs.get(i+1));
                }

                updatePossibleMoveLocations();
                testForCheck();
                //test for checkmate
                itsState.toggleActiveTeam();
            }

            itsState.setSelectedPieceIndex(-1);
            itsState.clearPossibleMoveIndexes();
        }

        return itsState;
    }

    private ArrayList<Integer> addMovePairs(int destination) {
        ArrayList<Integer> movePairs = new ArrayList<>();
        Square destinationSquare = itsBoard.getSquareAt(destination);

        if (destinationSquare.isOccupied()) {
            movePairs.add(destination);
            movePairs.add(-1);
        }

        movePairs.add(itsState.getSelectedPieceIndex());
        movePairs.add(destination);

        return movePairs;
    }

    private void movePiece(int origin, int destination) {
        final int FIRST_PERIM_INDEX = 64;
        final int FINAL_PERIM_INDEX = 100;

        Square originSquare = itsBoard.getSquareAt(origin);
        Square destinationSquare = NullSquare.generateNullSquare();
        ChessPiece movingPiece = originSquare.getOccupant();

        if (destination == -1) {
            for (int i = FIRST_PERIM_INDEX; i < FINAL_PERIM_INDEX; i++) {
                if (!itsBoard.getSquareAt(i).isOccupied()) {
                    destinationSquare = itsBoard.getSquareAt(i);
                    break;
                }
            }
        } else {
            destinationSquare = itsBoard.getSquareAt(destination);
        }

        movingPiece.setLocation(destinationSquare);
    }

    private void updatePossibleMoveLocations() {
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);
            piece.setPossibleMoveLocations(piece.generatePossibleMoveLocations());
        }
    }

    private void testForCheck() {
        int kingLocation = getKingLocation();

        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            if (piece.getTeam() == itsState.getActiveTeam()) {
                ArrayList<Square> moveLocations = piece.getPossibleMoveLocations();

                for (int j = 0; j < moveLocations.size(); j++) {
                    if (moveLocations.get(j).getNumericalLocation() == kingLocation) {
                        itsState.setCheck(true);
                        return;
                    }
                }
            }
        }

        itsState.setCheck(false);
    }

    private int getKingLocation() {
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            if (piece.getTeam() != itsState.getActiveTeam() && piece instanceof King) {
                return piece.getNumericalLocation();
            }
        }

        return -1;
    }
}
