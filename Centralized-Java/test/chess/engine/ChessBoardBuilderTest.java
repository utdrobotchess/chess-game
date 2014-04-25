package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessBoardBuilderTest {
    
    public ChessBoardBuilderTest() {
    }
    
    @Test
    private void testInteriorSquaresBuilt() {
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 64; i++) {
            Square actualSquare = board.getSquareAt(i);
            assert(actualSquare instanceof InteriorSquare);
            assertEquals(i, actualSquare.getLocation());
        }
    }
    
    @Test
    private void testPerimeterSquaresBuilt() {
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i =  64; i < 100; i++) {
            Square actualSquare = board.getSquareAt(i);
            assert(actualSquare instanceof PerimeterSquare);
            assertEquals(i, actualSquare.getLocation());
        }
    }
    
    @Test
    private void testNorthNeighbors() {
        int[] expectedNorthNeighbors = {
            65, 66, 67, 68, 69, 70, 71, 72,  0,  1,
             2,  3,  4,  5,  6,  7,  8,  9, 10, 11,
            12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
            22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
            32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
            42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
            52, 53, 54, 55, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 64, 73, 74, 75, 76, 77,
            78, 79, 80, 81, 82, 83, 84, 85, 86, 87,
            88, 56, 57, 58, 59, 60, 61, 62, 63, 89
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedNorthNeighbors[i], sq.getNeighborInDirection(0));
        }
    }
    
    @Test
    private void testNorthEastNeighbors() {
        int[] expectedNorthEastNeighbors = {
            66, 67, 68, 69, 70, 71, 72, 73,  1,  2,
             3,  4,  5,  6,  7, 75,  9, 10, 11, 12,
            13, 14, 15, 77, 17, 18, 19, 20, 21, 22,
            23, 79, 25, 26, 27, 28, 29, 30, 31, 81,
            33, 34, 35, 36, 37, 38, 39, 83, 41, 42,
            43, 44, 45, 46, 47, 85, 49, 50, 51, 52,
            53, 54, 55, 87, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 65, -1,  0, -1,  8, -1,
            16, -1, 24, -1, 32, -1, 40, -1, 48, -1,
            56, 57, 58, 59, 60, 61, 62, 63, 89, -1
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedNorthEastNeighbors[i], sq.getNeighborInDirection(1));
        }        
    }
    
    @Test
    private void testEastNeighbors() {
        int[] expectedEastNeighbors = {
             1,  2,  3,  4,  5,  6,  7, 75,  9, 10,
            11, 12, 13, 14, 15, 77, 17, 18, 19, 20,
            21, 22, 23, 79, 25, 26, 27, 28, 29, 30,
            31, 81, 33, 34, 35, 36, 37, 38, 39, 83,
            41, 42, 43, 44, 45, 46, 47, 85, 49, 50,
            51, 52, 53, 54, 55, 87, 57, 58, 59, 60,
            61, 62, 63, 89, 65, 66, 67, 68, 69, 70,
            71, 72, 73, -1,  0, -1,  8, -1, 16, -1,
            24, -1, 32, -1, 40, -1, 48, -1, 56, -1,
            91, 92, 93, 94, 95, 96, 97, 98, 99, -1
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedEastNeighbors[i], sq.getNeighborInDirection(2));
        }
    }
    
    @Test
    private void testSouthEastNeighbors() {
        int[] expectedSouthEastNeighbors = {
             9, 10, 11, 12, 13, 14, 15, 77, 17, 18,
            19, 20, 21, 22, 23, 79, 25, 26, 27, 28,
            29, 30, 31, 81, 33, 34, 35, 36, 37, 38,
            39, 83, 41, 42, 43, 44, 45, 46, 47, 85,
            49, 50, 51, 52, 53, 54, 55, 87, 57, 58,
            59, 60, 61, 62, 63, 89,  0,  1,  2,  3,
             4,  5,  6,  7, 75, -1,  8, -1, 16, -1,
            24, -1, 32, -1, 40, -1, 48, -1, 56, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedSouthEastNeighbors[i], sq.getNeighborInDirection(3));
        }
    }
    
    @Test
    private void testSouthNeighbors() {
        int[] expectedSouthNeighbors = {
             8,  9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
            38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
            58, 59, 60, 61, 62, 63, 91, 92, 93, 94,
            95, 96, 97, 98, 74,  0,  1,  2,  3,  4,
             5,  6,  7, 75, 76, 77, 78, 79, 80, 81,
            82, 83, 84, 85, 86, 87, 88, 89, 90, 99,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
            
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedSouthNeighbors[i], sq.getNeighborInDirection(4));
        }
    }
    
    @Test
    private void testSouthWestNeighbors() {
        int[] expectedSouthWestNeighbors = {
            76,  8,  9, 10, 11, 12, 13, 14, 78, 16,
            17, 18, 19, 20, 21, 22, 80, 24, 25, 26,
            27, 28, 29, 30, 82, 32, 33, 34, 35, 36,
            37, 38, 84, 40, 41, 42, 43, 44, 45, 46,
            86, 48, 49, 50, 51, 52, 53, 54, 88, 56,
            57, 58, 59, 60, 61, 62, 90, 91, 92, 93,
            94, 95, 96, 97, -1, 74,  0,  1,  2,  3,
             4,  5,  6,  7, -1, 15, -1, 23, -1, 31,
            -1, 39, -1, 47, -1, 55, -1, 63, -1, 98,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedSouthWestNeighbors[i], sq.getNeighborInDirection(5));
        }
    }
    
    @Test
    private void testWestNeighbors() {
        int[] expectedWestNeighbors = {
            74,  0,  1,  2,  3,  4,  5,  6, 76,  8,
             9, 10, 11, 12, 13, 14, 78, 16, 17, 18,
            19, 20, 21, 22, 80, 24, 25, 26, 27, 28,
            29, 30, 82, 32, 33, 34, 35, 36, 37, 38,
            84, 40, 41, 42, 43, 44, 45, 46, 86, 48,
            49, 50, 51, 52, 53, 54, 88, 56, 57, 58,
            59, 60, 61, 62, -1, 64, 65, 66, 67, 68,
            69, 70, 71, 72, -1,  7, -1, 15, -1, 23,
            -1, 31, -1, 39, -1, 47, -1, 55, -1, 63,
            -1, 90, 91, 92, 93, 94, 95, 96, 97, 98
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedWestNeighbors[i], sq.getNeighborInDirection(6));
        }
    }
    
    @Test
    private void testNorthWestNeighbors() {
        int[] expectedNorthWestNeighbors = {
            64, 65, 66, 67, 68, 69, 70, 71, 74,  0,
             1,  2,  3,  4,  5,  6, 76,  8,  9, 10,
            11, 12, 13, 14, 78, 16, 17, 18, 19, 20,
            21, 22, 80, 24, 25, 26, 27, 28, 29, 30,
            82, 32, 33, 34, 35, 36, 37, 38, 84, 40,
            41, 42, 43, 44, 45, 46, 86, 48, 49, 50,
            51, 52, 53, 54, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, 72, -1,  7, -1, 15,
            -1, 23, -1, 31, -1, 39, -1, 47, -1, 55,
            -1, 88, 56, 57, 58, 59, 60, 61, 62, 63
        };
        
        ChessBoard board = ChessBoard.generateChessBoard();
        
        for(int i = 0; i < 100; i++) {
            Square sq = board.getSquareAt(i);
            assertEquals(expectedNorthWestNeighbors[i], sq.getNeighborInDirection(7));
        }
    }
    
}
