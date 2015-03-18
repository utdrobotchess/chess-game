/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.game;

import org.junit.Test;
import org.junit.Assert;

public class ChessBoardTest
{
    @Test
    public void testNeighborAssignments()
    {
        final int TEST_SQUARES[] = {0 , 1 , 4 , 6 , 7 ,
                                    8 , 9 , 12, 14, 15,
                                    32, 33, 36, 38, 39,
                                    48, 49, 52, 54, 55,
                                    56, 57, 60, 62, 63};
        
        final int EXPECTED_NEIGHBORS[][] = {{-1, -1,  1,  9,  8, -1, -1, -1}, // 0
                                            {-1, -1,  2, 10,  9,  8,  0, -1}, // 1
                                            {-1, -1,  5, 13, 12, 11,  3, -1}, // 4
                                            {-1, -1,  7, 15, 14, 13,  5, -1}, // 6
                                            {-1, -1, -1, -1, 15, 14,  6, -1}, // 7
                                            { 0,  1,  9, 17, 16, -1, -1, -1}, // 8
                                            { 1,  2, 10, 18, 17, 16,  8,  0}, // 9
                                            { 4,  5, 13, 21, 20, 19, 11,  3}, // 12
                                            { 6,  7, 15, 23, 22, 21, 13,  5}, // 14
                                            { 7, -1, -1, -1, 23, 22, 14,  6}, // 15
                                            {24, 25, 33, 41, 40, -1, -1, -1}, // 32
                                            {25, 26, 34, 42, 41, 40, 32, 24}, // 33
                                            {28, 29, 37, 45, 44, 43, 35, 27}, // 36
                                            {30, 31, 39, 47, 46, 45, 37, 29}, // 38
                                            {31, -1, -1, -1, 47, 46, 38, 30}, // 39
                                            {40, 41, 49, 57, 56, -1, -1, -1}, // 48
                                            {41, 42, 50, 58, 57, 56, 48, 40}, // 49
                                            {44, 45, 53, 61, 60, 59, 51, 43}, // 52
                                            {46, 47, 55, 63, 62, 61, 53, 45}, // 54
                                            {47, -1, -1, -1, 63, 62, 54, 46}, // 55
                                            {48, 49, 57, -1, -1, -1, -1, -1}, // 56
                                            {49, 50, 58, -1, -1, -1, 56, 48}, // 57
                                            {52, 53, 61, -1, -1, -1, 59, 51}, // 60
                                            {54, 55, 63, -1, -1, -1, 61, 53}, // 62
                                            {55, -1, -1, -1, -1, -1, 62, 54}}; // 63
        
        ChessBoard board = new ChessBoard();

        Square squares[] = board.getSquares();

        for (int i = 0; i < TEST_SQUARES.length; i++) {
            for (int j = 0; j < 8; j++) {
                Square actualNeighbor = squares[TEST_SQUARES[i]].getNeighbor(j);
                int actualIntValue = -1;
                
                if (actualNeighbor != null)
                    actualIntValue = actualNeighbor.getIntLocation();
                
                Assert.assertEquals(EXPECTED_NEIGHBORS[i][j], actualIntValue);
            }
        }
    }
}
