package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class KnightTest {
    
    public KnightTest() {
    }

    @Test
    public void testSpawnAt() {
        ChessPiece greenKnight = Knight.spawnAt(InteriorSquare.generateInteriorSquareAt(1));
        ChessPiece orangeKnight = Knight.spawnAt(InteriorSquare.generateInteriorSquareAt(62));
        
        assertEquals(greenKnight.getIntegerLocation(), 1);
        assertEquals(orangeKnight.getIntegerLocation(), 62);
        
        assertEquals(greenKnight.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(orangeKnight.getTeam(), ChessPiece.Team.ORANGE);
        
        assert(greenKnight.hasNotMoved());
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKnight = Knight.spawnAt(board.getSquareAt(28));
        ChessPiece orangeKnight = Knight.spawnAt(board.getSquareAt(38));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(13));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(53));
        
        ArrayList<Square> greenKnightActualPossibleMoveLocations = greenKnight.getPossibleMoveLocations();
        ArrayList<Square> orangeKnightActualPossibleMoveLocations = orangeKnight.getPossibleMoveLocations();
        
        int[] greenKnightExpectedMoveLocations = {11, 18, 22, 34, 38, 43, 45};
        int[] orangeKnightExpectedMoveLocations = {21, 23, 28, 44, 55};
        
        assertEquals(7, greenKnightActualPossibleMoveLocations.size());
        assertEquals(5, orangeKnightActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 7; i++)
            assertEquals(greenKnightExpectedMoveLocations[i], greenKnightActualPossibleMoveLocations.get(i).getLocation());
        
        for(int i = 0; i < 5; i++)
            assertEquals(orangeKnightExpectedMoveLocations[i], orangeKnightActualPossibleMoveLocations.get(i).getLocation());
    }
}
