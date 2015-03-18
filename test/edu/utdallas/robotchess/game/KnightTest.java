/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class KnightTest
{
    @Test
    public void testMoveLocations()
    {
        ChessBoard board = new ChessBoard();
        
        final int K1_EXPECTED[] = {45, 47, 52};
        final int K2_EXPECTED[] = {13, 15, 36, 45, 47};
        final int K3_EXPECTED[] = {19, 21, 26, 30, 42, 46, 51, 53};
        final int K4_EXPECTED[] = {3, 5, 10, 14, 26, 35, 37};

        Knight k1 = new Knight(board.getSquareAt(62));
        Knight k2 = new Knight(board.getSquareAt(30));
        Knight k3 = new Knight(board.getSquareAt(36));
        Knight k4 = new Knight(board.getSquareAt(20));

        ArrayList<Square> k1Actual = k1.generateMoveLocations();
        ArrayList<Square> k2Actual = k2.generateMoveLocations();
        ArrayList<Square> k3Actual = k3.generateMoveLocations();
        ArrayList<Square> k4Actual = k4.generateMoveLocations();
        
        Assert.assertEquals(K1_EXPECTED.length, k1Actual.size());

        for (int i = 0; i < K1_EXPECTED.length; i++)
            Assert.assertEquals(K1_EXPECTED[i], k1Actual.get(i).getIntLocation());

        Assert.assertEquals(K2_EXPECTED.length, k2Actual.size());

        for (int i = 0; i < K2_EXPECTED.length; i++)
            Assert.assertEquals(K2_EXPECTED[i], k2Actual.get(i).getIntLocation());
        
        Assert.assertEquals(K3_EXPECTED.length, k3Actual.size());

        for (int i = 0; i < K3_EXPECTED.length; i++)
            Assert.assertEquals(K3_EXPECTED[i], k3Actual.get(i).getIntLocation());

        Assert.assertEquals(K4_EXPECTED.length, k4Actual.size());

        for (int i = 0; i < K4_EXPECTED.length; i++)
            Assert.assertEquals(K4_EXPECTED[i], k4Actual.get(i).getIntLocation());
    }
}
