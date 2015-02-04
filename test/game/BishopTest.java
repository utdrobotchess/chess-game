/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class BishopTest
{
    @Test
    public void testMoveLocations()
    {
        ChessBoard board = new ChessBoard();

        final int B1_EXPECTED[] = {29, 36, 43, 48, 50};
        final int B2_EXPECTED[] = {11, 15, 20, 22, 36, 38, 43, 50, 57};
        final int B3_EXPECTED[] = {29, 31, 45, 47, 52, 59};
        final int B4_EXPECTED[] = {9, 11, 16, 20};

        Bishop b1 = new Bishop(board.getSquareAt(57));
        Bishop b2 = new Bishop(board.getSquareAt(29));
        Bishop b3 = new Bishop(board.getSquareAt(38));
        Bishop b4 = new Bishop(board.getSquareAt(2));

        ArrayList<Square> b1Actual = b1.generateMoveLocations();
        ArrayList<Square> b2Actual = b2.generateMoveLocations();
        ArrayList<Square> b3Actual = b3.generateMoveLocations();
        ArrayList<Square> b4Actual = b4.generateMoveLocations();

        assertEquals(B1_EXPECTED.length, b1Actual.size());

        for (int i = 0; i < B1_EXPECTED.length; i++)
            assertEquals(B1_EXPECTED[i], b1Actual.get(i).getIntLocation());

        assertEquals(B2_EXPECTED.length, b2Actual.size());

        for (int i = 0; i < B2_EXPECTED.length; i++)
            assertEquals(B2_EXPECTED[i], b2Actual.get(i).getIntLocation());

        assertEquals(B3_EXPECTED.length, b3Actual.size());

        for (int i = 0; i < B3_EXPECTED.length; i++)
            assertEquals(B3_EXPECTED[i], b3Actual.get(i).getIntLocation());

        assertEquals(B4_EXPECTED.length, b4Actual.size());

        for (int i = 0; i < B4_EXPECTED.length; i++)
            assertEquals(B4_EXPECTED[i], b4Actual.get(i).getIntLocation());

    }
}
