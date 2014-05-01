package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class KingTest {
    
    public KingTest() {
    }

    @Test
    public void testSpawn() {
        ChessPiece greenKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(4));
        ChessPiece orangeKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(60));
        
        assertEquals(greenKing.getIntegerLocation(), 4);
        assertEquals(orangeKing.getIntegerLocation(), 60);
        
        assertEquals(greenKing.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(orangeKing.getTeam(), ChessPiece.Team.ORANGE);
        
        assert(greenKing.hasNotMoved());
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKing = King.spawnAt(board.getSquareAt(28));
        ChessPiece orangeKing = King.spawnAt(board.getSquareAt(36));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(29));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(44));
        
        ArrayList<Square> greenKingActualPossibleMoveLocations = greenKing.generatePossibleMoveLocations();
        ArrayList<Square> orangeKingActualPossibleMoveLocations = orangeKing.generatePossibleMoveLocations();
        
        int[] greenKingExpectedMoveLocations = {19, 20, 21, 27, 35, 36, 37};
        int[] orangeKingExpectedMoveLocations = {27, 28, 29, 35, 37, 43, 45};
        
        assertEquals(7, greenKingActualPossibleMoveLocations.size());
        assertEquals(7, orangeKingActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 7; i++) {
            assertEquals(greenKingExpectedMoveLocations[i], greenKingActualPossibleMoveLocations.get(i).getLocation());
            assertEquals(orangeKingExpectedMoveLocations[i], orangeKingActualPossibleMoveLocations.get(i).getLocation());
        }
    }
    
    @Test
    public void testCastling1() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKing = King.spawnAt(board.getSquareAt(4));
        ChessPiece greenRook1 = Rook.spawnAt(board.getSquareAt(0));
        ChessPiece greenRook2 = Rook.spawnAt(board.getSquareAt(7));
        
        ArrayList<Square> greenKingActualPossibleMoveLocations = greenKing.generatePossibleMoveLocations();
        
        int[] greenKingExpectedMoveLocations = {1, 3, 5, 6, 11, 12, 13};
        
        assertEquals(7, greenKingActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 7; i++)
            assertEquals(greenKingExpectedMoveLocations[i], greenKingActualPossibleMoveLocations.get(i).getLocation());
    }
    
    @Test
    public void testCasting2() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece orangeKing = King.spawnAt(board.getSquareAt(60));
        ChessPiece orangeRook1 = Rook.spawnAt(board.getSquareAt(56));
        ChessPiece orangeRook2 = Rook.spawnAt(board.getSquareAt(63));
        
        orangeRook1.setNumberOfPriorMoves(1);
        
        ArrayList<Square> orangeKingActualPossibleMoveLocations = orangeKing.generatePossibleMoveLocations();
        
        int[] orangeKingExpectedMoveLocations = {51, 52, 53, 59, 61, 62};
        
        assertEquals(6, orangeKingActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 6; i++)
            assertEquals(orangeKingExpectedMoveLocations[i], orangeKingActualPossibleMoveLocations.get(i).getLocation());
    }
    
    @Test
    public void testCasting3() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece orangeKing = King.spawnAt(board.getSquareAt(60));
        ChessPiece orangeRook1 = Rook.spawnAt(board.getSquareAt(56));
        ChessPiece orangeRook2 = Rook.spawnAt(board.getSquareAt(63));
        
        orangeKing.setNumberOfPriorMoves(1);
        
        ArrayList<Square> orangeKingActualPossibleMoveLocations = orangeKing.generatePossibleMoveLocations();
        
        int[] orangeKingExpectedMoveLocations = {51, 52, 53, 59, 61};
        
        assertEquals(5, orangeKingActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 5; i++)
            assertEquals(orangeKingExpectedMoveLocations[i], orangeKingActualPossibleMoveLocations.get(i).getLocation());
    }
    
    @Test
    public void testCasting4() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKing = King.spawnAt(board.getSquareAt(4));
        ChessPiece greenRook1 = Rook.spawnAt(board.getSquareAt(0));
        ChessPiece greenRook2 = Rook.spawnAt(board.getSquareAt(7));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(2));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(45));
        orangePawn.setLocation(board.getSquareAt(6));
        
        ArrayList<Square> greenKingActualPossibleMoveLocations = greenKing.generatePossibleMoveLocations();
        
        int[] greenKingExpectedMoveLocations = {3, 5, 11, 12, 13};
        
        assertEquals(5, greenKingActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 5; i++)
            assertEquals(greenKingExpectedMoveLocations[i], greenKingActualPossibleMoveLocations.get(i).getLocation());
    }
}
