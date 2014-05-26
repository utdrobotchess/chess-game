package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Tests the functionality of a knight, including generation and determination
 * of possible move locations
 * @author Ryan J. Marcotte
 */
public class KnightTest {

    @Test
    public void testSpawnAt() {
        ChessPiece greenKnight = Knight.spawnAt(InteriorSquare.generateInteriorSquareAt(1));
        ChessPiece orangeKnight = Knight.spawnAt(InteriorSquare.generateInteriorSquareAt(62));
        
        assertEquals(greenKnight.getNumericalLocation(), 1);
        assertEquals(orangeKnight.getNumericalLocation(), 62);
        
        assertEquals(greenKnight.getTeam(), Team.GREEN);
        assertEquals(orangeKnight.getTeam(), Team.ORANGE);
        
        assert(greenKnight.hasNotMoved());
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKnight = Knight.spawnAt(board.getSquareAt(28));
        ChessPiece orangeKnight = Knight.spawnAt(board.getSquareAt(38));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(13));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(53));
        
        ArrayList<Square> greenKnightActualPossibleMoveLocations = greenKnight.generatePossibleMoveLocations();
        ArrayList<Square> orangeKnightActualPossibleMoveLocations = orangeKnight.generatePossibleMoveLocations();
        
        int[] greenKnightExpectedMoveLocations = {11, 18, 22, 34, 38, 43, 45};
        int[] orangeKnightExpectedMoveLocations = {21, 23, 28, 44, 55};
        
        assertEquals(7, greenKnightActualPossibleMoveLocations.size());
        assertEquals(5, orangeKnightActualPossibleMoveLocations.size());
        
		Square actualLocation;

        for(int i = 0; i < 7; i++) {
			actualLocation = greenKnightActualPossibleMoveLocations.get(i);
		 	assertEquals(greenKnightExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}
        
        for(int i = 0; i < 5; i++) {
			actualLocation = orangeKnightActualPossibleMoveLocations.get(i);
		 	assertEquals(orangeKnightExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}
    }
}
