package edu.utdallas.robotchess.game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class KingTest
{
    @Test
    public void testBasicMoveLocations()
    {
        ChessBoard board = new ChessBoard();

        final int K1_EXPECTED[] = {51, 52, 53, 59, 61};
        final int K2_EXPECTED[] = {19, 20, 21, 27, 29, 35, 36, 37};
        final int K3_EXPECTED[] = {26, 27, 28, 34, 42, 43, 44};
        final int K4_EXPECTED[] = {27, 28, 29, 37, 43, 44, 45};

        King k1 = new King(board.getSquareAt(60), 28);
        King k2 = new King(board.getSquareAt(28), 4);
        King k3 = new King(board.getSquareAt(35), 16);
        King k4 = new King(board.getSquareAt(36), 17);

        ArrayList<Square> k1Actual = k1.generateMoveLocations();
        ArrayList<Square> k2Actual = k2.generateMoveLocations();
        ArrayList<Square> k3Actual = k3.generateMoveLocations();
        ArrayList<Square> k4Actual = k4.generateMoveLocations();
        
        Assert.assertEquals(K1_EXPECTED.length, k1Actual.size());

        for (int i = 0; i < K1_EXPECTED.length; i++)
            Assert.assertEquals(K1_EXPECTED[i], k1Actual.get(i).toInt());

        Assert.assertEquals(K2_EXPECTED.length, k2Actual.size());

        for (int i = 0; i < K2_EXPECTED.length; i++)
            Assert.assertEquals(K2_EXPECTED[i], k2Actual.get(i).toInt());
        
        Assert.assertEquals(K3_EXPECTED.length, k3Actual.size());

        for (int i = 0; i < K3_EXPECTED.length; i++)
            Assert.assertEquals(K3_EXPECTED[i], k3Actual.get(i).toInt());

        Assert.assertEquals(K4_EXPECTED.length, k4Actual.size());

        for (int i = 0; i < K4_EXPECTED.length; i++)
            Assert.assertEquals(K4_EXPECTED[i], k4Actual.get(i).toInt());
    }
}
