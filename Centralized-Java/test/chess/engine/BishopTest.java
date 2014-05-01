package chess.engine;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Owner
 */
public class BishopTest {
    
    public BishopTest() {
    }

    @Test
    public void testSpawnBishopAt() {
        ChessPiece greenBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(2));
        ChessPiece greenBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(5));
        ChessPiece orangeBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(58));
        ChessPiece orangeBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(61));
        
        assertEquals(greenBishop1.getIntegerLocation(), 2);
        assertEquals(greenBishop2.getIntegerLocation(), 5);
        assertEquals(orangeBishop1.getIntegerLocation(), 58);
        assertEquals(orangeBishop2.getIntegerLocation(), 61);
        
        assertEquals(greenBishop1.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(greenBishop2.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(orangeBishop1.getTeam(), ChessPiece.Team.ORANGE);
        assertEquals(orangeBishop2.getTeam(), ChessPiece.Team.ORANGE);
        
        assertEquals(greenBishop1.getNumberOfPriorMoves(), 0);
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenBishop = Bishop.spawnAt(board.getSquareAt(11));
        ChessPiece orangeBishop = Bishop.spawnAt(board.getSquareAt(47));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(25));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(61));
        
        ArrayList<Square> greenBishopActualPossibleMoveLocations = greenBishop.generatePossibleMoveLocations();
        ArrayList<Square> orangeBishopActualPossibleMoveLocations = orangeBishop.generatePossibleMoveLocations();
        
        int[] greenBishopExpectedMoveLocations = {2, 4, 18, 20, 29, 38, 47};
        int[] orangeBishopExpectedMoveLocations = {11, 20, 29, 38, 54};
        
        assertEquals(7, greenBishopActualPossibleMoveLocations.size());
        assertEquals(5, orangeBishopActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 7; i++)
            assertEquals(greenBishopExpectedMoveLocations[i], greenBishopActualPossibleMoveLocations.get(i).getLocation());
        
        for(int i = 0; i < 5; i++)
            assertEquals(orangeBishopExpectedMoveLocations[i], orangeBishopActualPossibleMoveLocations.get(i).getLocation());
    }
}
