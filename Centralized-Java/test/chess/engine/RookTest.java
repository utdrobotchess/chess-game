package chess.engine;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class RookTest {
    
    public RookTest() {
    }

    @Test
    public void testSpawnAt() {
        ChessPiece greenRook1 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(0));
        ChessPiece greenRook2 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(7));
        ChessPiece orangeRook1 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(56));
        ChessPiece orangeRook2 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(63));
        
        assertEquals(greenRook1.getIntegerLocation(), 0);
        assertEquals(greenRook2.getIntegerLocation(), 7);
        assertEquals(orangeRook1.getIntegerLocation(), 56);
        assertEquals(orangeRook2.getIntegerLocation(), 63);
        
        assertEquals(greenRook1.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(greenRook2.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(orangeRook1.getTeam(), ChessPiece.Team.ORANGE);
        assertEquals(orangeRook2.getTeam(), ChessPiece.Team.ORANGE);
        
        assert(greenRook1.hasNotMoved());
        
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenRook = Rook.spawnAt(board.getSquareAt(20));
        ChessPiece orangeRook = Rook.spawnAt(board.getSquareAt(60));
        ChessPiece greenPawn1 = Pawn.spawnAt(board.getSquareAt(19));
        ChessPiece greenPawn2 = Pawn.spawnAt(board.getSquareAt(4));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(56));
        
        ArrayList<Square> greenRookActualPossibleMoveLocations = greenRook.generatePossibleMoveLocations();
        ArrayList<Square> orangeRookActualPossibleMoveLocations = orangeRook.generatePossibleMoveLocations();
        
        int[] greenRookExpectedMoveLocations = {12, 21, 22, 23, 28, 36, 44, 52, 60};
        int[] orangeRookExpectedMoveLocations = {20, 28, 36, 44, 52, 57, 58, 59, 61, 62, 63};
        
        assertEquals(9, greenRookActualPossibleMoveLocations.size());
        assertEquals(11, orangeRookActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 9; i++)
            assertEquals(greenRookExpectedMoveLocations[i], greenRookActualPossibleMoveLocations.get(i).getLocation());
        
        for(int i = 0; i < 11; i++)
            assertEquals(orangeRookExpectedMoveLocations[i], orangeRookActualPossibleMoveLocations.get(i).getLocation());
    }
}
