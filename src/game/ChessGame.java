/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;
import java.util.Collections;

public class ChessGame
{
    ChessPiece allPieces[];

    final int NUM_PIECES = 32;

    final int ROOK_IDS[] = {0, 7, 24, 31};
    final int KNIGHT_IDS[] = {1, 6, 25, 30};
    final int BISHOP_IDS[] = {2, 5, 26, 29};
    final int QUEEN_IDS[] = {3, 27};
    final int KING_IDS[] = {4, 28};
    final int PAWN_IDS[] = {8 ,  9, 10, 11, 12, 13, 14, 15,
                            16, 17, 18, 19, 20, 21, 22, 23};

    final int ROOK_INITIAL_POSITIONS[] = {0, 7, 56, 63};
    final int KNIGHT_INITIAL_POSITIONS[] = {1, 6, 57, 62};
    final int BISHOP_INITIAL_POSITIONS[] = {2, 5, 58, 61};
    final int QUEEN_INITIAL_POSITIONS[] = {3, 59};
    final int KING_INITIAL_POSITIONS[] = {4, 60};
    final int PAWN_INITIAL_POSITIONS[] = {8 ,  9, 10, 11, 12, 13, 14, 15,
                                          48, 49, 50, 51, 52, 53, 54, 55};

    ChessBoard board;
  
    public ChessGame()
    {
        board = new ChessBoard();
        initializePieces();
    }

    public ChessGame(ChessBoard board, ChessPiece[] pieces)
    {
        this.board = board;
        allPieces = pieces;
    }

    private void initializePieces()
    {
        allPieces = new ChessPiece[NUM_PIECES];

        for (int i = 0; i < ROOK_IDS.length; i++) {
            Square position = board.getSquareAt(ROOK_INITIAL_POSITIONS[i]);
            int id = ROOK_IDS[i];
            allPieces[id] = new Rook(position, id);
        }

        for (int i = 0; i < KNIGHT_IDS.length; i++) {
            Square position = board.getSquareAt(KNIGHT_INITIAL_POSITIONS[i]);
            int id = KNIGHT_IDS[i];
            allPieces[id] = new Knight(position, id);
        }

        for (int i = 0; i < BISHOP_IDS.length; i++) {
            Square position = board.getSquareAt(BISHOP_INITIAL_POSITIONS[i]);
            int id = BISHOP_IDS[i];
            allPieces[id] = new Bishop(position, id);
        }

        for (int i = 0; i < QUEEN_IDS.length; i++) {
            Square position = board.getSquareAt(QUEEN_INITIAL_POSITIONS[i]);
            int id = QUEEN_IDS[i];
            allPieces[id] = new Queen(position, id);
        }

        for (int i = 0; i < KING_IDS.length; i++) {
            Square position = board.getSquareAt(KING_INITIAL_POSITIONS[i]);
            int id = KING_IDS[i];
            allPieces[id] = new King(position, id);
        }

        for (int i = 0; i < PAWN_IDS.length; i++) {
            Square position = board.getSquareAt(PAWN_INITIAL_POSITIONS[i]);
            int id = PAWN_IDS[i];
            allPieces[id] = new Pawn(position, id);
        }
    }

    protected ChessGame copyGame()
    {
        final int NUM_SQUARES = 64;

        ChessBoard copiedBoard = new ChessBoard();
        ChessPiece copiedPieces[] = new ChessPiece[NUM_PIECES];

        for (int i = 0; i < NUM_PIECES; i++) {
            copiedPieces[i] = allPieces[i].copyPiece();

            if (copiedPieces[i].isActive()) {
                int intLocation = allPieces[i].getLocation().getIntLocation();
                copiedPieces[i].setLocation(copiedBoard.getSquareAt(intLocation));
            }
        }
        
        ChessGame copiedGame = new ChessGame(copiedBoard, copiedPieces);

        return copiedGame;
    }

    protected ArrayList<Square> generateMoveLocations(ChessPiece piece, boolean removeChecks)
    {
        ArrayList<Square> moveLocations = piece.generateMoveLocations();
        // add castling here
        
        if (removeChecks) {
            for (int i = 0; i < moveLocations.size(); i++) {
                ChessGame copiedGame = copyGame();
                ChessPiece copiedPieces[] = copiedGame.getAllPieces();
                ChessBoard copiedBoard = copiedGame.getBoard();
                ChessPiece copiedTestPiece = copiedPieces[piece.getID()];
                Square copiedDestinationSquare = copiedBoard.getSquareAt(moveLocations.get(i).getIntLocation());

                copiedTestPiece.moveTo(copiedDestinationSquare);
                if (copiedGame.isInCheck(copiedTestPiece.getTeam())) {
                    moveLocations.remove(i--);
                }
            }
        } 

        return moveLocations;
    }
    
    protected ArrayList<Square> generateCastlingMoves(ChessPiece king)
    {
        ArrayList<Square> castlingMoves = new ArrayList<>();

        if (!king.hasNotMoved() || isInCheck(king.getTeam()))
            return castlingMoves;

        int kingSideSquares[] = {5, 6};
        int queenSideSquares[] = {1, 2, 3};

        if (king.getTeam() == Team.WHITE) {
            for (int i = 0; i < kingSideSquares.length; i++)
                kingSideSquares[i] += 56;
            
            for (int i = 0; i < queenSideSquares.length; i++)
                queenSideSquares[i] += 56;
        }
        
        if (!board.getSquareAt(kingSideSquares[0]).isOccupied() &&
            !board.getSquareAt(kingSideSquares[1]).isOccupied()) { 
            int castlingRookIndex = king.getTeam() == Team.BLACK ? 7 : 31;
            ChessPiece castlingRook = allPieces[castlingRookIndex];

            if (castlingRook.hasNotMoved())
                castlingMoves.add(board.getSquareAt(kingSideSquares[1]));
        }

        if (!board.getSquareAt(queenSideSquares[0]).isOccupied() &&
            !board.getSquareAt(queenSideSquares[1]).isOccupied() &&
            !board.getSquareAt(queenSideSquares[2]).isOccupied()) {
            int castlingRookIndex = king.getTeam() == Team.BLACK ? 0 : 24;
            ChessPiece castlingRook = allPieces[castlingRookIndex];

            if (castlingRook.hasNotMoved())
                castlingMoves.add(board.getSquareAt(queenSideSquares[1]));
        }

        Collections.sort(castlingMoves);

        return castlingMoves;
    }

    protected ChessPiece[] getAllPieces()
    {
        return allPieces;
    }

    protected ChessBoard getBoard()
    {
        return board;
    }

    private ChessPiece getKing(Team team)
    {
        if (team == Team.WHITE)
            return allPieces[28];
        else
            return allPieces[4];
    }

    protected boolean isInCheck(Team team)
    {
        ChessPiece king = getKing(team);
        
        for (int i = 0; i < allPieces.length; i++) {
            if (allPieces[i].getTeam() != team && allPieces[i].isActive()) {
                ArrayList<Square> moveLocations = allPieces[i].generateMoveLocations();
                if (moveLocations.contains(king.getLocation()))
                    return true;
            }
        }
        
        return false;
    }

    protected boolean isInCheckmate(Team team)
    {
        for (int i = 0; i < NUM_PIECES; i++) {
            if (allPieces[i].getTeam() == team && allPieces[i].isActive()) {
                ArrayList<Square> moveLocations = generateMoveLocations(allPieces[i], true);

                if (moveLocations.size() > 0)
                    return false;
            }
        }
        
        return true;
    }
}
