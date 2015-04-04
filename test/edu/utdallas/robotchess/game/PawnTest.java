package edu.utdallas.robotchess.game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class PawnTest
{
    @Test
    public void testSimpleOpeningMove()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(9), 0);

        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        Assert.assertEquals(2, p1Actual.size());
        Assert.assertEquals(17, p1Actual.get(0).toInt());
        Assert.assertEquals(25, p1Actual.get(1).toInt());
    }

    @Test
    public void testOpeningMoveWithObstruction()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(54), 16);
        Pawn p2 = new Pawn(board.getSquareAt(14), 0);
        Pawn p3 = new Pawn(board.getSquareAt(38), 17);
        Pawn p4 = new Pawn(board.getSquareAt(22), 1);

        ArrayList<Square> p1Actual = p1.generateMoveLocations();
        ArrayList<Square> p2Actual = p2.generateMoveLocations();

        Assert.assertEquals(1, p1Actual.size());
        Assert.assertEquals(46, p1Actual.get(0).toInt());
        
        Assert.assertEquals(0, p2Actual.size());
    }
    
    @Test
    public void testCapture()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(39), 16);
        Pawn p2 = new Pawn(board.getSquareAt(30), 0);
        Pawn p3 = new Pawn(board.getSquareAt(37), 17);

        final int P1_EXPECTED[] = {23, 30, 31};
        final int P2_EXPECTED[] = {37, 38, 39, 46};
        final int P3_EXPECTED[] = {21, 29, 30};

        ArrayList<Square> p1Actual = p1.generateMoveLocations();
        Assert.assertEquals(3, p1Actual.size());

        for (int i = 0; i < p1Actual.size(); i++)
            Assert.assertEquals(P1_EXPECTED[i], p1Actual.get(i).toInt());

        ArrayList<Square> p2Actual = p2.generateMoveLocations();
        Assert.assertEquals(4, p2Actual.size());

        for (int i = 0; i < p2Actual.size(); i++)
            Assert.assertEquals(P2_EXPECTED[i], p2Actual.get(i).toInt());

        ArrayList<Square> p3Actual = p3.generateMoveLocations();
        Assert.assertEquals(3, p3Actual.size());
        
        for (int i = 0; i < p3Actual.size(); i++)
            Assert.assertEquals(P3_EXPECTED[i], p3Actual.get(i).toInt());
    }

    @Test
    public void testNoCaptureSameTeam()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(52), 16);
        Pawn p2 = new Pawn(board.getSquareAt(43), 17);
        Pawn p3 = new Pawn(board.getSquareAt(45), 18);

        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        Assert.assertEquals(2, p1Actual.size());
        Assert.assertEquals(36, p1Actual.get(0).toInt());
        Assert.assertEquals(44, p1Actual.get(1).toInt());
    }

    @Test
    public void testAlreadyMoved()
    {
        ChessBoard board = new ChessBoard();

        Pawn p1 = new Pawn(board.getSquareAt(52), 16);
        p1.setHasNotMoved(false);
        
        ArrayList<Square> p1Actual = p1.generateMoveLocations();

        Assert.assertEquals(1, p1Actual.size());
        Assert.assertEquals(44, p1Actual.get(0).toInt());
    }
}
    
