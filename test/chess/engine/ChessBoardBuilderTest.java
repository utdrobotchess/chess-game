/*
 *
 * @author Ryan J. Marcotte
 */
package chess.engine;

import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class ChessBoardBuilderTest {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_INTERIOR_SQUARES = 64;
    private static final int NUM_PERIMETER_SQUARES = 36;
    private static final int TOTAL_SQUARES = 100;
    private ChessBoard board;
	private String errorMessage;
	private Square testSquare;
    private Square actualNeighbor;

	@Before
	public void initialize() {
		board = ChessBoard.generateChessBoard();
	}

	/*
	 * Ensures that squares 0-63 have been built as interior squares
	 */
    @Test
    public void testInteriorSquaresBuilt() {
        Square actualSquare;

        logger.log(Level.WARNING, "Begin testInteriorSquaresBuilt() - ChessBoardBuilderTest");

		for(int i = 0; i < NUM_INTERIOR_SQUARES; i++) {
            actualSquare = board.getSquareAt(i);
            assert(actualSquare instanceof InteriorSquare);
            assertEquals(i, actualSquare.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testInteriorSquaresBuilts() - ChessBoardBuilderTest");
    }

	/*
	 * Ensures that squares 64-99 have been built as perimeter squares
	 */
    @Test
    public void testPerimeterSquaresBuilt() {
		Square actualSquare;

        logger.log(Level.WARNING, "Begin testPerimeterSquaresBuilt() - ChessBoardBuilderTest");

        for (int i =  TOTAL_SQUARES - NUM_PERIMETER_SQUARES; i < TOTAL_SQUARES; i++) {
            actualSquare = board.getSquareAt(i);
            assert(actualSquare instanceof PerimeterSquare);
            assertEquals(i, actualSquare.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testPerimeterSquaresBuilt() - ChessBoardBuilderTest");
    }

    @Test
    public void testNorthNeighbors() {

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

        logger.log(Level.WARNING, "Begin testNorthNeighbors() - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(0);
            errorMessage = "Unexpected north neighbor at location " + i;
            assertEquals(errorMessage, expectedNorthNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testNorthNeighbors() - ChessBoardBuilderTest");
    }

    @Test
    public void testNorthEastNeighbors() {
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

        logger.log(Level.WARNING, "Begin testNorthEastNeighbors - ChessBoard BuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(1);
            errorMessage = "Unexpected northeast neighbor at location " + i;
            assertEquals(errorMessage, expectedNorthEastNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testNorthEastNeighbors - ChessBoardBuilderTest");
    }

    @Test
    public void testEastNeighbors() {
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

        logger.log(Level.WARNING, "Begin testEastNeighbors - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(2);
            errorMessage = "Unexpected east neighbor at location " + i;
            assertEquals(errorMessage, expectedEastNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testEastNeighbors - ChessBoardBuilderTest");
    }

    @Test
    public void testSouthEastNeighbors() {
        int[] expectedSouthEastNeighbors = {
             9, 10, 11, 12, 13, 14, 15, 77, 17, 18,
            19, 20, 21, 22, 23, 79, 25, 26, 27, 28,
            29, 30, 31, 81, 33, 34, 35, 36, 37, 38,
            39, 83, 41, 42, 43, 44, 45, 46, 47, 85,
            49, 50, 51, 52, 53, 54, 55, 87, 57, 58,
            59, 60, 61, 62, 63, 89, 92, 93, 94, 95,
            96, 97, 98, 99,  0,  1,  2,  3,  4,  5,
            6,  7,  75, -1,  8, -1, 16, -1, 24, -1,
            32, -1, 40, -1, 48, -1, 56, -1, 91, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };

        logger.log(Level.WARNING, "Begin testSouthEastNeighbors() - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(3);
            errorMessage = "Unexpected southeast neighbor at location " + i;
            assertEquals(errorMessage, expectedSouthEastNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testSouthEastNeighbors() - ChessBoardBuilderTest");
    }

    @Test
    public void testSouthNeighbors() {
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

        logger.log(Level.WARNING, "Begin testSouthNeighbors() - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(4);
            errorMessage = "Unexpected south neighbor at location " + i;
            assertEquals(errorMessage, expectedSouthNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testSouthNeighbors() - ChessBoardBuilderTest");
    }

    @Test
    public void testSouthWestNeighbors() {
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

        logger.log(Level.WARNING, "Begin testSouthWestNeighbors() - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(5);
            errorMessage = "Unexpected southwest neighbor at location " + i;
            assertEquals(errorMessage, expectedSouthWestNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testSouthWestNeighborst() - ChessBoardBuilderTest");
    }

    @Test
    public void testWestNeighbors() {
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

        logger.log(Level.WARNING, "Begin testWestNeighbors() - ChessBoardBuilderTest");

		for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(6);
            errorMessage = "Unexpected west neighbor at location " + i;
            assertEquals(errorMessage, expectedWestNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testWestNeighborst() - ChessBoardBuilderTest");
    }

    @Test
    public void testNorthWestNeighbors() {
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

        logger.log(Level.WARNING, "Begin testNorthWestNeighbors() - ChessBoardBuilderTest");

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            testSquare = board.getSquareAt(i);
            actualNeighbor = testSquare.getNeighborInDirection(7);
            errorMessage = "Unexpected northwest neighbor at location" + i;
            assertEquals(errorMessage, expectedNorthWestNeighbors[i],
                         actualNeighbor.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testNorthWestNeighbors() - ChessBoardBuilderTest");
    }

}
