package chess.engine;

import java.util.logging.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of the chess game builder, including the placement
 * of pieces.
 * @author Ryan J. Marcotte
 */
public class ChessGameBuilderTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

    @Test
    public void testPiecePlacement() {
        logger.log(Level.WARNING, "Begin testPiecePlacement() - ChessGameBuilderTest");

        ChessGame game = ChessGame.setupGame();
        ChessBoard board = game.getBoard();

        int[] expectedRookLocations = {0, 7, 56, 63};
        int[] expectedKnightLocations = {1, 6, 57, 62};
        int[] expectedBishopLocations = {2, 5, 58, 61};
        int[] expectedQueenLocations = {3, 59};
        int[] expectedKingLocations = {4, 60};
        int[] expectedPawnLocations = {8, 9, 10, 11, 12, 13, 14, 15, 48, 49, 50,
                                        51, 52, 53, 54, 55};

        for(int i = 0; i < 4; i++) {
            assert(board.getSquareAt(expectedRookLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedKnightLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedBishopLocations[i]).isOccupied());
        }

        for(int i = 0; i < 2; i++) {
            assert(board.getSquareAt(expectedQueenLocations[i]).isOccupied());
            assert(board.getSquareAt(expectedKingLocations[i]).isOccupied());
        }

        for(int i = 0; i < 16; i++)
            assert(board.getSquareAt(expectedPawnLocations[i]).isOccupied());

        for(int i = 16; i < 48; i++)
            assert(!board.getSquareAt(i).isOccupied());

		for(int i = 0; i < 16; i++) {
			assertEquals(Team.GREEN, board.getSquareAt(i).getOccupyingTeam());
			assertEquals(Team.ORANGE, board.getSquareAt(i+48).getOccupyingTeam());
		}

        logger.log(Level.WARNING, "End testPiecePlacement() - ChessGameBuilderTest");
    }

}
