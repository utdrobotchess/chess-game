/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class QueenTest
{
    @Test
    public void testMoveLocations()
    {
        ChessBoard board = new ChessBoard();

        final int Q1_EXPECTED[] = {26, 30, 35, 38, 44, 46, 53, 54,
                                   55, 56, 57, 58, 59, 60, 61, 63};
        final int Q2_EXPECTED[] = {3 , 6 , 12, 14, 21, 22, 23, 27, 28, 29,
                                   31, 37, 38, 39, 44, 46, 51, 54, 58, 62};
        final int Q3_EXPECTED[] = {2 , 5 , 8 , 10, 12, 17, 18, 19,
                                   24, 25, 27, 28, 29, 33, 34, 35,
                                   40, 42, 44, 50, 53, 58, 62};

        Queen q1 = new Queen(board.getSquareAt(62));
        Queen q2 = new Queen(board.getSquareAt(30));
        Queen q3 = new Queen(board.getSquareAt(26));

        ArrayList<Square> q1Actual = q1.generateMoveLocations();
        ArrayList<Square> q2Actual = q2.generateMoveLocations();
        ArrayList<Square> q3Actual = q3.generateMoveLocations();

        Assert.assertEquals(Q1_EXPECTED.length, q1Actual.size());

        for (int i = 0; i < Q1_EXPECTED.length; i++)
            Assert.assertEquals(Q1_EXPECTED[i], q1Actual.get(i).getIntLocation());
 
        Assert.assertEquals(Q2_EXPECTED.length, q2Actual.size());

        for (int i = 0; i < Q2_EXPECTED.length; i++)
            Assert.assertEquals(Q2_EXPECTED[i], q2Actual.get(i).getIntLocation());

        Assert.assertEquals(Q3_EXPECTED.length, q3Actual.size());

        for (int i = 0; i < Q3_EXPECTED.length; i++)
            Assert.assertEquals(Q3_EXPECTED[i], q3Actual.get(i).getIntLocation());
      }
}
