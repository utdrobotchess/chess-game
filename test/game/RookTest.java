/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class RookTest
{
    @Test
    public void testMoveLocations()
    {
        ChessBoard board = new ChessBoard();

        final int R1_EXPECTED[] = {30, 38, 46, 54, 56, 57, 58, 59, 60, 61, 63};
        final int R2_EXPECTED[] = {6, 14, 22, 27, 28, 29, 31, 38, 46, 54, 62};
        final int R3_EXPECTED[] = {2, 10, 18, 24, 25, 27, 28, 29, 34};
        final int R4_EXPECTED[] = {26, 32, 33, 35, 36, 37, 38, 39, 42, 50, 58};

        Rook r1 = new Rook(board.getSquareAt(62));
        Rook r2 = new Rook(board.getSquareAt(30));
        Rook r3 = new Rook(board.getSquareAt(26));
        Rook r4 = new Rook(board.getSquareAt(34));

        ArrayList<Square> r1Actual = r1.generateMoveLocations();
        ArrayList<Square> r2Actual = r2.generateMoveLocations();
        ArrayList<Square> r3Actual = r3.generateMoveLocations();
        ArrayList<Square> r4Actual = r4.generateMoveLocations();

        Assert.assertEquals(R1_EXPECTED.length, r1Actual.size());

        for (int i = 0; i < R1_EXPECTED.length; i++)
            Assert.assertEquals(R1_EXPECTED[i], r1Actual.get(i).getIntLocation());

        Assert.assertEquals(R2_EXPECTED.length, r2Actual.size());

        for (int i = 0; i < R2_EXPECTED.length; i++)
            Assert.assertEquals(R2_EXPECTED[i], r2Actual.get(i).getIntLocation());
        
        Assert.assertEquals(R3_EXPECTED.length, r3Actual.size());

        for (int i = 0; i < R3_EXPECTED.length; i++)
            Assert.assertEquals(R3_EXPECTED[i], r3Actual.get(i).getIntLocation());
        
        Assert.assertEquals(R4_EXPECTED.length, r4Actual.size());

        for (int i = 0; i < R4_EXPECTED.length; i++)
            Assert.assertEquals(R4_EXPECTED[i], r4Actual.get(i).getIntLocation());
    }
}
