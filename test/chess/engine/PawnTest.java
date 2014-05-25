package chess.engine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Tests the functionality of a Pawn, including
 * @author Ryan J. Marcotte 
 */
public class PawnTest {
    
	/*
	 * Ensures that a Pawn spawns correctly, with the proper location assignment,
	 * team, and number of prior moves.
	 */
    @Test
    public void testSpawnPawnAt() {
        Pawn p1 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(8));
        Pawn p2 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(50));
        
        assertEquals(p1.getNumericalLocation(), 8);
        assertEquals(p2.getNumericalLocation(), 50);
        
        assertEquals(p1.getTeam(), Team.GREEN);
        assertEquals(p2.getTeam(), Team.ORANGE);
        
        assert(p1.hasNotMoved());
    }
   
	/*
	 * Ensures that the opening moves are correctly determined in situations 
	 * without any obstructions.
	 */	
    @Test
    public void testPossibleOpeningMoves() {
        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn p1 = Pawn.spawnAt(board.getSquareAt(8));
        Pawn p2 = Pawn.spawnAt(board.getSquareAt(54));
        
        ArrayList<Square> p1ActualPossibleMoveLocations = p1.generatePossibleMoveLocations();
        ArrayList<Square> p2ActualPossibleMoveLocations = p2.generatePossibleMoveLocations();
        
        assertEquals(2, p1ActualPossibleMoveLocations.size());
        assertEquals(16, p1ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(24, p1ActualPossibleMoveLocations.get(1).getNumericalLocation());
        assertEquals(2, p2ActualPossibleMoveLocations.size());
        assertEquals(38, p2ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(46, p2ActualPossibleMoveLocations.get(1).getNumericalLocation());
    }
    
	/*
	 * Ensures that the opening moves are correctly determined in situations
	 * with obstructions either one or two squares away.
	 */
    @Test
    public void testPossibleOpeningMovesWithObstructions() {
        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn p1 = Pawn.spawnAt(board.getSquareAt(10));
        Pawn p2 = Pawn.spawnAt(board.getSquareAt(26));
        Pawn p3 = Pawn.spawnAt(board.getSquareAt(53));
        Pawn p4 = Pawn.spawnAt(board.getSquareAt(45));
        
        ArrayList<Square> p1ActualPossibleMoveLocations = p1.generatePossibleMoveLocations();
        ArrayList<Square> p3ActualPossibleMoveLocations = p3.generatePossibleMoveLocations();
        
        assertEquals(1, p1ActualPossibleMoveLocations.size());
        assertEquals(18, p1ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(0, p3ActualPossibleMoveLocations.size());
    }
    
	/*
	 * Ensures that a pawn could capture a piece from an opposing team that is diagonally in front of it
	 */
    @Test
    public void testPossibleCapturingMoves() {
        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn greenPawn1 = Pawn.spawnAt(board.getSquareAt(10));
        Pawn greenPawn2 = Pawn.spawnAt(board.getSquareAt(11));
        Pawn greenPawn3 = Pawn.spawnAt(board.getSquareAt(12));
		Pawn greenPawn4 = Pawn.spawnAt(board.getSquareAt(19));
        Pawn orangePawn1 = Pawn.spawnAt(board.getSquareAt(50));
        Pawn orangePawn2 = Pawn.spawnAt(board.getSquareAt(51));
        
        orangePawn2.setLocation(board.getSquareAt(17));
        greenPawn2.setLocation(board.getSquareAt(41));
        greenPawn3.setLocation(board.getSquareAt(43));
        
        ArrayList<Square> greenPawn1ActualPossibleMoveLocations = greenPawn1.generatePossibleMoveLocations();
        ArrayList<Square> orangePawn1ActualPossibleMoveLocations = orangePawn1.generatePossibleMoveLocations();
        
		//should be able to capture at 17 or move forward to 18 or 26
        int[] greenPawn1ExpectedMoveLocations = {17, 18, 26};

		//should be able to capture at 41 or 42 or move forward to 42 or 34
        int[] orangePawn1ExpectedMoveLocations = {34, 41, 42, 43};
        
        assertEquals(3, greenPawn1ActualPossibleMoveLocations.size());
        assertEquals(4, orangePawn1ActualPossibleMoveLocations.size());
        
		Square actualSquareLocation;

        for(int i = 0; i < 3; i++) {
			actualSquareLocation = greenPawn1ActualPossibleMoveLocations.get(i);
            assertEquals(greenPawn1ExpectedMoveLocations[i], actualSquareLocation.getNumericalLocation());
		}

        for(int i = 0; i < 4; i++) {
			actualSquareLocation = orangePawn1ActualPossibleMoveLocations.get(i);
			assertEquals(orangePawn1ExpectedMoveLocations[i], actualSquareLocation.getNumericalLocation());
		}
    }
}
