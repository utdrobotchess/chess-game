package chess.engine;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a bishop, including spawning and determining possible move locations
 * @author Ryan J. Marcotte
 */
public class BishopTest {

	/*
	 * Ensures that a Bishop is generated correctly, with the proper location, team, and prior move status
	 */
    @Test
    public void testSpawnBishopAt() {
        ChessPiece greenBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(2));
        ChessPiece greenBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(5));
        ChessPiece orangeBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(58));
        ChessPiece orangeBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(61));
        
        assertEquals(greenBishop1.getNumericalLocation(), 2);
        assertEquals(greenBishop2.getNumericalLocation(), 5);
        assertEquals(orangeBishop1.getNumericalLocation(), 58);
        assertEquals(orangeBishop2.getNumericalLocation(), 61);
        
        assertEquals(greenBishop1.getTeam(), Team.GREEN);
        assertEquals(greenBishop2.getTeam(), Team.GREEN);
        assertEquals(orangeBishop1.getTeam(), Team.ORANGE);
        assertEquals(orangeBishop2.getTeam(), Team.ORANGE);
        
        assert(greenBishop1.hasNotMoved());
    }
    
	/*
	 * Ensures that a Bishop can correctly determine the list of moves available to it, including cases of
	 * encountering the edge of the board, a teammate, and an opposing piece
	 */
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
        
		Square actualLocation;

        for(int i = 0; i < 7; i++) {
            actualLocation = greenBishopActualPossibleMoveLocations.get(i);
			assertEquals(greenBishopExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}

        for(int i = 0; i < 5; i++) {
			actualLocation = orangeBishopActualPossibleMoveLocations.get(i);
			assertEquals(orangeBishopExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}
    }
}
