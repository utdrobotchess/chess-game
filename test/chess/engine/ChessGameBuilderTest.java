/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class ChessGameBuilderTest {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int NUM_INTERIOR_SQUARES = 64;
    private static final int TOTAL_PIECES = 32;

    @Test
    public void testPiecePlacement() {
        logger.log(Level.WARNING, "Begin testPiecePlacement() - ChessGameBuilderTest");

        ChessPiece occupant;

        int[] expectedRookLocations = {0, 7, 56, 63};
        int[] expectedKnightLocations = {1, 6, 57, 62};
        int[] expectedBishopLocations = {2, 5, 58, 61};
        int[] expectedQueenLocations = {3, 59};
        int[] expectedKingLocations = {4, 60};
        int[] expectedPawnLocations = {8, 9, 10, 11, 12, 13, 14, 15, 48, 49, 50,
                                        51, 52, 53, 54, 55};

        ChessGame game = ChessGame.setupGame();
        ChessBoard board = game.getBoard();

        for (int i = 0; i < expectedRookLocations.length; i++) {
            assert(board.getSquareAt(expectedRookLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedKnightLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedBishopLocations[i]).isOccupied());
        }

        for (int i = 0; i < expectedQueenLocations.length; i++) {
            assert(board.getSquareAt(expectedQueenLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedKingLocations[i]).isOccupied());
        }

        for (int i = 0; i < expectedPawnLocations.length; i++) {
            assert(board.getSquareAt(expectedPawnLocations[i]).isOccupied());
        }

        for (int i = TOTAL_PIECES / 2; i < (NUM_INTERIOR_SQUARES - TOTAL_PIECES / 2); i++) {
            assert(!board.getSquareAt(i).isOccupied());
        }

		for (int i = 0; i < TOTAL_PIECES / 2; i++) {
            occupant = board.getSquareAt(i).getOccupant();
			assertEquals(Team.GREEN, occupant.getTeam());
            occupant = board.getSquareAt(i + 48).getOccupant();
			assertEquals(Team.ORANGE, occupant.getTeam());
		}

        logger.log(Level.WARNING, "End testPiecePlacement() - ChessGameBuilderTest");
    }

}
