/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class PawnTest
{
    @Test
    public void testSimpleOpeningMove()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(9));

        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        assertEquals(2, p1Actual.size());
        assertEquals(17, p1Actual.get(0).getIntLocation());
        assertEquals(25, p1Actual.get(1).getIntLocation());
    }

    @Test
    public void testOpeningMoveWithObstruction()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(54));
        Pawn p2 = new Pawn(board.getSquareAt(14));
        Pawn p3 = new Pawn(board.getSquareAt(38));
        Pawn p4 = new Pawn(board.getSquareAt(22));

        ArrayList<Square> p1Actual = p1.generateMoveLocations();
        ArrayList<Square> p2Actual = p2.generateMoveLocations();

        assertEquals(1, p1Actual.size());
        assertEquals(46, p1Actual.get(0).getIntLocation());
        
        assertEquals(0, p2Actual.size());
    }
    
    @Test
    public void testCapture()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(39));
        Pawn p2 = new Pawn(board.getSquareAt(30));
        Pawn p3 = new Pawn(board.getSquareAt(37));

        final int P1_EXPECTED[] = {23, 30, 31};
        final int P2_EXPECTED[] = {37, 38, 39, 46};
        final int P3_EXPECTED[] = {21, 29, 30};

        ArrayList<Square> p1Actual = p1.generateMoveLocations();
        assertEquals(3, p1Actual.size());

        for (int i = 0; i < p1Actual.size(); i++)
            assertEquals(P1_EXPECTED[i], p1Actual.get(i).getIntLocation());

        ArrayList<Square> p2Actual = p2.generateMoveLocations();
        assertEquals(4, p2Actual.size());

        for (int i = 0; i < p2Actual.size(); i++)
            assertEquals(P2_EXPECTED[i], p2Actual.get(i).getIntLocation());

        ArrayList<Square> p3Actual = p3.generateMoveLocations();
        assertEquals(3, p3Actual.size());
        
        for (int i = 0; i < p3Actual.size(); i++)
            assertEquals(P3_EXPECTED[i], p3Actual.get(i).getIntLocation());
    }

    @Test
    public void testNoCaptureSameTeam()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(52));
        Pawn p2 = new Pawn(board.getSquareAt(43));
        Pawn p3 = new Pawn(board.getSquareAt(45));

        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        assertEquals(2, p1Actual.size());
        assertEquals(36, p1Actual.get(0).getIntLocation());
        assertEquals(44, p1Actual.get(1).getIntLocation());
    }

    @Test
    public void testAlreadyMoved()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(52));
        p1.setHasNotMoved(false);
        
        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        assertEquals(1, p1Actual.size());
        assertEquals(44, p1Actual.get(0).getIntLocation());
    }
}
    
