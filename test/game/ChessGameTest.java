/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class ChessGameTest
{
    public static void main(String[] args)
    {
        ChessGameTest test = new ChessGameTest();
        test.testNoCastlingIfObstructions();
    }
    
    @Test
    public void testInitialPositions()
    {
        ChessGame game = new ChessGame();

        final int EXPECTED_INITIAL_POSITIONS[] = {0 ,  1,  2,  3,  4,  5,  6,  7,
                                                  8 ,  9, 10, 11, 12, 13, 14, 15,
                                                  48, 49, 50, 51, 52, 53, 54, 55,
                                                  56, 57, 58, 59, 60, 61, 62, 63};

        final int EXPECTED_ROOK_IDS[] = {0, 7, 24, 31};
        final int EXPECTED_KNIGHT_IDS[] = {1, 6, 25, 30};
        final int EXPECTED_BISHOP_IDS[] = {2, 5, 26, 29};
        final int EXPECTED_QUEEN_IDS[] = {3, 27};
        final int EXPECTED_KING_IDS[] = {4, 28};
        final int EXPECTED_PAWN_IDS[] = {8 ,  9, 10, 11, 12, 13, 14, 15,
                                         16, 17, 18, 19, 20, 21, 22, 23};
        
        ChessPiece pieces[] = game.getAllPieces();

        for (int i = 0; i < EXPECTED_INITIAL_POSITIONS.length; i++) {
            Assert.assertEquals(EXPECTED_INITIAL_POSITIONS[i],
                                pieces[i].getLocation().getIntLocation());
        }

        for (int i = 0; i < 16; i++)
            Assert.assertEquals(Team.BLACK, pieces[i].getTeam());

        for (int i = 16; i < 32; i++)
            Assert.assertEquals(Team.WHITE, pieces[i].getTeam());

        for (int i = 0; i < EXPECTED_ROOK_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_ROOK_IDS[i]];
            Assert.assertTrue(testPiece instanceof Rook);
        }

        for (int i = 0; i < EXPECTED_KNIGHT_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_KNIGHT_IDS[i]];
            Assert.assertTrue(testPiece instanceof Knight);
        }

        for (int i = 0; i < EXPECTED_BISHOP_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_BISHOP_IDS[i]];
            Assert.assertTrue(testPiece instanceof Bishop);
        }

        for (int i = 0; i < EXPECTED_QUEEN_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_QUEEN_IDS[i]];
            Assert.assertTrue(testPiece instanceof Queen);
        }

        for (int i = 0; i < EXPECTED_KING_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_KING_IDS[i]];
            Assert.assertTrue(testPiece instanceof King);
        }

        for (int i = 0; i < EXPECTED_PAWN_IDS.length; i++) {
            ChessPiece testPiece = pieces[EXPECTED_PAWN_IDS[i]];
            Assert.assertTrue(testPiece instanceof Pawn);
        }
    }

    @Test
    public void testMove()
    {
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        pieces[12].moveTo(board.getSquareAt(28));

        Assert.assertEquals(28, pieces[12].getLocation().getIntLocation());
        Assert.assertFalse(board.getSquareAt(12).isOccupied());
        Assert.assertEquals(null, board.getSquareAt(12).getOccupant());
        Assert.assertTrue(board.getSquareAt(28).isOccupied());
        Assert.assertEquals(12, board.getSquareAt(28).getOccupant().getID());
    }

    @Test
    public void testTurn()
    {

    }

    @Test
    public void testCapture()
    {
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        pieces[12].moveTo(board.getSquareAt(28));
        pieces[19].moveTo(board.getSquareAt(35));
        pieces[12].moveTo(board.getSquareAt(35));

        Assert.assertEquals(35, pieces[12].getLocation().getIntLocation());
        Assert.assertEquals(null, pieces[19].getLocation());

        Assert.assertFalse(board.getSquareAt(28).isOccupied());
        Assert.assertEquals(null, board.getSquareAt(28).getOccupant());
        
        Assert.assertTrue(board.getSquareAt(35).isOccupied());
        Assert.assertEquals(12, board.getSquareAt(35).getOccupant().getID());

        Assert.assertTrue(pieces[12].isActive());
        Assert.assertFalse(pieces[19].isActive());
    }
    
    @Test
    public void testIdentifyCheck()
    {
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        pieces[20].moveTo(board.getSquareAt(44));
        pieces[11].moveTo(board.getSquareAt(19));
        pieces[29].moveTo(board.getSquareAt(25));

        Assert.assertTrue(game.isInCheck(Team.BLACK));

        pieces[3].moveTo(board.getSquareAt(11));

        Assert.assertFalse(game.isInCheck(Team.BLACK));
    }

    @Test
    public void testRemoveMovesResultingInCheck()
    {
        final int EXPECTED_MOVE_LOCATIONS[][] = {{},
                                                 {11, 18},
                                                 {11},
                                                 {11},
                                                 {},
                                                 {},
                                                 {},
                                                 {},
                                                 {},
                                                 {},
                                                 {18},
                                                 {},
                                                 {},
                                                 {},
                                                 {},
                                                 {}};
        
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        pieces[20].moveTo(board.getSquareAt(44));
        pieces[11].moveTo(board.getSquareAt(19));
        pieces[29].moveTo(board.getSquareAt(25));

        for (int i = 0; i < EXPECTED_MOVE_LOCATIONS.length; i++) {
            ArrayList<Square> actualMoveLocations = game.generateMoveLocations(pieces[i], true);
            Assert.assertEquals(EXPECTED_MOVE_LOCATIONS[i].length,
                                actualMoveLocations.size());
            for (int j = 0; j < EXPECTED_MOVE_LOCATIONS[i].length; i++)
                Assert.assertEquals(EXPECTED_MOVE_LOCATIONS[i][j],
                                    actualMoveLocations.get(j).getIntLocation());
        }
    }

    @Test
    public void testIdentifyCheckMate()
    {
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        pieces[20].moveTo(board.getSquareAt(36));
        pieces[14].moveTo(board.getSquareAt(22));
        pieces[29].moveTo(board.getSquareAt(34));
        pieces[14].moveTo(board.getSquareAt(30));
        pieces[27].moveTo(board.getSquareAt(45));
        pieces[14].moveTo(board.getSquareAt(38));

        Assert.assertFalse(game.isInCheckmate(Team.BLACK));
        
        pieces[27].moveTo(board.getSquareAt(13));

        Assert.assertTrue(game.isInCheckmate(Team.BLACK));
        Assert.assertFalse(game.isInCheckmate(Team.WHITE));
    }

    @Test
    public void testNoCastlingIfPriorMoves()
    {
        final int movingPieces[] = {29, 30, 28, 28, 1, 2, 3, 0, 0};
        final int castlingKing[] = {28, 28, 28, 28, 4, 4, 4, 4, 4};
        final int moveDestinations[] = {45, 46, 44, 60, 17, 18, 19, 16, 0};
        final int expectedCastlingMoves[][] = {{}, {62}, {}, {}, {}, 
                                               {}, {2}, {}, {}};
        
        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();
        
         ArrayList<Square> actualCastlingMoves;

        for (int i = 0; i < movingPieces.length; i++) {
            pieces[movingPieces[i]].moveTo(board.getSquareAt(moveDestinations[i]));
            actualCastlingMoves = game.generateCastlingMoves(pieces[castlingKing[i]]);
            Assert.assertEquals(expectedCastlingMoves[i].length,
                                actualCastlingMoves.size());
            
            for (int j = 0; j < expectedCastlingMoves[i].length; j++)
                Assert.assertEquals(expectedCastlingMoves[i][j], 
                                    actualCastlingMoves.get(j).getIntLocation());
        }
    }

    @Test
    public void testNoCastlingIfObstructions()
    {
        final int movingPieces[] = {20, 29, 30};
        final int moveDestinations[] = {36, 34, 47};
        final int expectedCastlingMoves[][] = {{}, {}, {62}};

        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();
        
        ArrayList<Square> actualCastlingMoves;

        for (int i = 0; i < movingPieces.length; i++) {
            pieces[movingPieces[i]].moveTo(board.getSquareAt(moveDestinations[i]));
            actualCastlingMoves = game.generateCastlingMoves(pieces[28]);
            Assert.assertEquals(expectedCastlingMoves[i].length,
                                actualCastlingMoves.size());
            
            for (int j = 0; j < expectedCastlingMoves[i].length; j++)
                Assert.assertEquals(expectedCastlingMoves[i][j], 
                                    actualCastlingMoves.get(j).getIntLocation());
        }
    }

    @Test
    public void testCastlingBothSides()
    {
        final int movingPieces[] = {1, 2, 3, 5, 6};
        final int moveDestinations[] = {17, 18, 19, 21, 22};
        final int expectedCastlingMoves[][] = {{}, {}, {2}, {2}, {2, 6}};

        ChessGame game = new ChessGame();

        ChessPiece pieces[] = game.getAllPieces();
        ChessBoard board = game.getBoard();

        ArrayList<Square> actualCastlingMoves;

        for (int i = 0; i < movingPieces.length; i++) {
            pieces[movingPieces[i]].moveTo(board.getSquareAt(moveDestinations[i]));
            actualCastlingMoves = game.generateCastlingMoves(pieces[4]);
            Assert.assertEquals(expectedCastlingMoves[i].length,
                                actualCastlingMoves.size());

            for (int j = 0; j < expectedCastlingMoves[i].length; j++)
                Assert.assertEquals(expectedCastlingMoves[i][j],
                                    actualCastlingMoves.get(j).getIntLocation());
        }
    }

    @Test
    public void testCastlingPassThrough()
    {

    }

    @Test
    public void testCastlingResultCheck()
    {

    }

    // en passant

    // pawn promotion

    // draws?
}
