package chess.engine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Ryan J. Marcotte 
 */
public class PawnTest {
    
    public PawnTest() {
    }
    
    @Test
    public void testSpawnPawnAt() {
        Pawn p1 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(8));
        Pawn p2 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(12));
        Pawn p3 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(50));
        Pawn p4 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(53));
        
        assertEquals(p1.getIntegerLocation(), 8);
        assertEquals(p2.getIntegerLocation(), 12);
        assertEquals(p3.getIntegerLocation(), 50);
        assertEquals(p4.getIntegerLocation(), 53);
        
        assertEquals(p1.getTeam(), Team.GREEN);
        assertEquals(p2.getTeam(), Team.GREEN);
        assertEquals(p3.getTeam(), Team.ORANGE);
        assertEquals(p4.getTeam(), Team.ORANGE);
        
        assertEquals(p1.getNumberOfPriorMoves(), 0);
    }
    
    @Test
    public void testPossibleOpeningMoves() {
        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn p1 = Pawn.spawnAt(board.getSquareAt(8));
        Pawn p2 = Pawn.spawnAt(board.getSquareAt(54));
        
        ArrayList<Square> p1ActualPossibleMoveLocations = p1.generatePossibleMoveLocations();
        ArrayList<Square> p2ActualPossibleMoveLocations = p2.generatePossibleMoveLocations();
        
        assertEquals(2, p1ActualPossibleMoveLocations.size());
        assertEquals(16, p1ActualPossibleMoveLocations.get(0).getLocation());
        assertEquals(24, p1ActualPossibleMoveLocations.get(1).getLocation());
        assertEquals(2, p2ActualPossibleMoveLocations.size());
        assertEquals(38, p2ActualPossibleMoveLocations.get(0).getLocation());
        assertEquals(46, p2ActualPossibleMoveLocations.get(1).getLocation());
    }
    
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
        assertEquals(18, p1ActualPossibleMoveLocations.get(0).getLocation());
        assertEquals(0, p3ActualPossibleMoveLocations.size());
    }
    
    @Test
    public void testPossibleCapturingMoves() {
        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn greenPawn1 = Pawn.spawnAt(board.getSquareAt(10));
        Pawn greenPawn2 = Pawn.spawnAt(board.getSquareAt(11));
        Pawn greenPawn3 = Pawn.spawnAt(board.getSquareAt(12));
        Pawn orangePawn1 = Pawn.spawnAt(board.getSquareAt(50));
        Pawn orangePawn2 = Pawn.spawnAt(board.getSquareAt(51));
        
        orangePawn2.setLocation(board.getSquareAt(17));
        greenPawn2.setLocation(board.getSquareAt(41));
        greenPawn3.setLocation(board.getSquareAt(43));
        
        ArrayList<Square> greenPawn1ActualPossibleMoveLocations = greenPawn1.generatePossibleMoveLocations();
        ArrayList<Square> orangePawn1ActualPossibleMoveLocations = orangePawn1.generatePossibleMoveLocations();
        
        int[] greenPawn1ExpectedMoveLocations = {17, 18, 26};
        int[] orangePawn1ExpectedMoveLocations = {34, 41, 42, 43};
        
        assertEquals(3, greenPawn1ActualPossibleMoveLocations.size());
        assertEquals(4, orangePawn1ActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 3; i++)
            assertEquals(greenPawn1ExpectedMoveLocations[i], greenPawn1ActualPossibleMoveLocations.get(i).getLocation());
        
        for(int i = 0; i < 4; i++)
            assertEquals(orangePawn1ExpectedMoveLocations[i], orangePawn1ActualPossibleMoveLocations.get(i).getLocation());
    }
}
