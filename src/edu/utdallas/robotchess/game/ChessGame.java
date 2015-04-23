package edu.utdallas.robotchess.game;

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

    ChessBoard board;
    Team activeTeam;

    public ChessGame()
    {
        initializePieces();
        activeTeam = Team.ORANGE;
    }

    public ChessGame(ChessBoard board, ChessPiece[] pieces)
    {
        this.board = board;
        allPieces = pieces;
    }

    private void initializePieces()
    {
        int[] pieceLocations = {0 ,  1,  2,  3,  4,  5,  6,  7,
                                8 ,  9, 10, 11, 12, 13, 14, 15,
                                48, 49, 50, 51, 52, 53, 54, 55,
                                56, 57, 58, 59, 60, 61, 62, 63};
        initializePieces(pieceLocations);
    }

    public void initializePieces(int[] pieceLocations)
    {
        board = new ChessBoard();

        allPieces = new ChessPiece[NUM_PIECES];

        for (int i = 0; i < ROOK_IDS.length; i++) {
            int id = ROOK_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
            allPieces[id] = new Rook(position, id);
        }

        for (int i = 0; i < KNIGHT_IDS.length; i++) {
            int id = KNIGHT_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
            allPieces[id] = new Knight(position, id);
        }

        for (int i = 0; i < BISHOP_IDS.length; i++) {
            int id = BISHOP_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
            allPieces[id] = new Bishop(position, id);
        }

        for (int i = 0; i < QUEEN_IDS.length; i++) {
            int id = QUEEN_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
            allPieces[id] = new Queen(position, id);
        }

        for (int i = 0; i < KING_IDS.length; i++) {
            int id = KING_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
            allPieces[id] = new King(position, id);
        }

        for (int i = 0; i < PAWN_IDS.length; i++) {
            int id = PAWN_IDS[i];
            Square position = board.getSquareAt(pieceLocations[id]);
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
                int intLocation = allPieces[i].getIntLocation();
                copiedPieces[i].setLocation(copiedBoard.getSquareAt(intLocation));
            }
        }

        ChessGame copiedGame = new ChessGame(copiedBoard, copiedPieces);
        copiedGame.setActiveTeam(activeTeam);

        return copiedGame;
    }

    public ArrayList<Square> generateMoveLocations(ChessPiece piece)
    {
        ArrayList<Square> moveLocations = piece.generateMoveLocations();
        // add castling here

        for (int i = 0; i < moveLocations.size(); i++) {
            ChessGame copiedGame = copyGameAndMovePiece(piece, moveLocations.get(i));

            if (copiedGame.isInCheck(piece.getTeam()))
                    moveLocations.remove(i--);
        }

        return moveLocations;
    }

    public ChessGame copyGameAndMovePiece(int pieceID, int destination)
    {
        return copyGameAndMovePiece(allPieces[pieceID],
                                    getBoardSquareAt(destination));
    }

    private ChessGame copyGameAndMovePiece(ChessPiece piece, Square destination)
    {
        ChessGame copiedGame = copyGame();
        ChessPiece copiedPieces[] = copiedGame.getAllPieces();
        ChessBoard copiedBoard = copiedGame.getBoard();
        ChessPiece copiedTestPiece = copiedPieces[piece.getID()];
        Square copiedDestinationSquare = copiedBoard.getSquareAt(destination.toInt());
        copiedTestPiece.moveTo(copiedDestinationSquare);
        copiedGame.toggleActiveTeam();

        return copiedGame;
    }

    protected ArrayList<Square> generateCastlingMoves(ChessPiece king)
    {
        ArrayList<Square> castlingMoves = new ArrayList<>();

        if (!king.hasNotMoved() || isInCheck(king.getTeam()))
            return castlingMoves;

        int kingSideCastle = determineCastleOnSide(king, true);
        if (kingSideCastle != -1)
            castlingMoves.add(board.getSquareAt(kingSideCastle));

        int queenSideCastle = determineCastleOnSide(king, false);
        if (queenSideCastle != -1)
            castlingMoves.add(board.getSquareAt(queenSideCastle));

        Collections.sort(castlingMoves);

        return castlingMoves;
    }

    private int determineCastleOnSide(ChessPiece king, boolean kingSide)
    {
        int sideSquareIndexes[] = getSideSquareIndexes(king, kingSide);

        if (isSideSquareOccupied(sideSquareIndexes))
            return -1;

        if (castlingRookHasMoved(king, kingSide))
            return -1;

        if (isInCheckWhileCastling(king, sideSquareIndexes))
            return -1;

        return sideSquareIndexes[1];
    }

    private int[] getSideSquareIndexes(ChessPiece king, boolean kingSide)
    {
        final int NUM_KING_SIDE_SQUARES = 2;
        final int NUM_QUEEN_SIDE_SQUARES = 3;
        int numSideSquares = kingSide ? NUM_KING_SIDE_SQUARES : NUM_QUEEN_SIDE_SQUARES;
        int sideSquareIndexes[] = new int[numSideSquares];

        if (kingSide) {
            sideSquareIndexes[0] = 5;
            sideSquareIndexes[1] = 6;
        } else {
            sideSquareIndexes[0] = 1;
            sideSquareIndexes[1] = 2;
            sideSquareIndexes[2] = 3;
        }

        if (king.getTeam() == Team.ORANGE)
            for (int i = 0; i < sideSquareIndexes.length; i++)
                sideSquareIndexes[i] += 56;

         return sideSquareIndexes;
    }

    private boolean isSideSquareOccupied(int sideSquareIndexes[])
    {
        for (int i = 0; i < sideSquareIndexes.length; i++)
            if (board.getSquareAt(sideSquareIndexes[i]).isOccupied())
                return true;

        return false;
    }

    private boolean castlingRookHasMoved(ChessPiece king, boolean kingSide)
    {
        int castlingRookIndex = getCastlingRookIndex(king, kingSide);
        ChessPiece castlingRook = allPieces[castlingRookIndex];

        if (castlingRook.hasNotMoved())
            return false;

        return true;
    }

    private int getCastlingRookIndex(ChessPiece king, boolean kingSide)
    {
        int castlingRookIndex = kingSide ? 7 : 0;

        if (king.getTeam() == Team.ORANGE)
            castlingRookIndex += 16;

        return castlingRookIndex;
    }

    private boolean isInCheckWhileCastling(ChessPiece king, int sideSquareIndexes[])
    {
        for (int i = 0; i < sideSquareIndexes.length; i++) {
            ChessGame copiedGame = copyGameAndMovePiece(king,
                                                        board.getSquareAt(sideSquareIndexes[i]));

            if (copiedGame.isInCheck(king.getTeam()))
                return true;
        }

        return false;
    }

    public ChessPiece[] getAllPieces()
    {
        return allPieces;
    }

    public ArrayList<ChessPiece> getActivePieces()
    {
        ArrayList<ChessPiece> activePieces = new ArrayList<>();

        for (int i = 0; i < allPieces.length; i++)
            if (allPieces[i].isActive())
                activePieces.add(allPieces[i]);

        return activePieces;
    }

    public int[] getPieceLocations()
    {
        int[] locations = new int[32];

        for (int i = 0; i < locations.length; i++)
            locations[i] = allPieces[i].getIntLocation();

        return locations;
    }

    public Team getActiveTeam()
    {
        return activeTeam;
    }

    protected ChessBoard getBoard()
    {
        return board;
    }

    public Square getBoardSquareAt(int index)
    {
        return board.getSquareAt(index);
    }

    private ChessPiece getKing(Team team)
    {
        if (team == Team.ORANGE)
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

    public boolean isInCheckmate()
    {
        return isInCheckmate(activeTeam);
    }

    protected boolean isInCheckmate(Team team)
    {
        for (int i = 0; i < NUM_PIECES; i++) {
            if (allPieces[i].getTeam() == team && allPieces[i].isActive()) {
                ArrayList<Square> moveLocations = generateMoveLocations(allPieces[i]);

                if (moveLocations.size() > 0)
                    return false;
            }
        }

        return true;
    }

    public void setActiveTeam(Team team)
    {
        activeTeam = team;
    }

    public void toggleActiveTeam()
    {
        if (activeTeam == Team.ORANGE)
            activeTeam = Team.GREEN;
        else
            activeTeam = Team.ORANGE;
    }
}
