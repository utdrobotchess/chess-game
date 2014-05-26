package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Tests the functionality of a king, including generation and determination of 
 * move locations
 * @author Ryan J. Marcotte
 */
public class KingTest {

    @Test
    public void testSpawn() {
        ChessPiece greenKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(4));
        ChessPiece orangeKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(60));
        
        assertEquals(greenKing.getNumericalLocation(), 4);
        assertEquals(orangeKing.getNumericalLocation(), 60);
        
        assertEquals(greenKing.getTeam(), Team.GREEN);
        assertEquals(orangeKing.getTeam(), Team.ORANGE);
        
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
        
		Square actualLocation;

        for(int i = 0; i < 7; i++) {
			actualLocation = greenKingActualPossibleMoveLocations.get(i);
            assertEquals(greenKingExpectedMoveLocations[i], actualLocation.getNumericalLocation());

			actualLocation = orangeKingActualPossibleMoveLocations.get(i);
            assertEquals(orangeKingExpectedMoveLocations[i], actualLocation.getNumericalLocation());
        }
    }
}
