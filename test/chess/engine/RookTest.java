/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class RookTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

	/*
	 * Ensures that a Rook spawns correctly, with the proper location, team, and number of
	 * initial moves.
	 */
    @Test
    public void testSpawnAt() {
        logger.log(Level.WARNING, "Begin testSpawnAt() - RookTest");

        ChessPiece greenRook1 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(0));
        ChessPiece greenRook2 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(7));
        ChessPiece orangeRook1 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(56));
        ChessPiece orangeRook2 = Rook.spawnAt(InteriorSquare.generateInteriorSquareAt(63));

        assertEquals(0, greenRook1.getNumericalLocation());
        assertEquals(7, greenRook2.getNumericalLocation());
        assertEquals(56, orangeRook1.getNumericalLocation());
        assertEquals(63, orangeRook2.getNumericalLocation());

        assertEquals(Team.GREEN, greenRook1.getTeam());
        assertEquals(Team.GREEN, greenRook2.getTeam());
        assertEquals(Team.ORANGE, orangeRook1.getTeam());
        assertEquals(Team.ORANGE, orangeRook2.getTeam());

        assert(greenRook1.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawnAt() - RookTest");
    }

	/*
	 * Ensures that a Rook can correctly determine the possible locations to which it can move
	 */
    @Test
    public void testPossibleMoveLocations() {
        logger.log(Level.WARNING, "Begin testPossibleMoveLocations() - RookTest");

        int[] greenRookExpectedMoveLocations = {12, 21, 22, 23, 28, 36, 44, 52, 60};
        int[] orangeRookExpectedMoveLocations = {20, 28, 36, 44, 52, 57, 58, 59, 61, 62, 63};

		Square actualMoveLocation;

        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenRook = Rook.spawnAt(board.getSquareAt(20));
        ChessPiece orangeRook = Rook.spawnAt(board.getSquareAt(60));
        ChessPiece greenPawn1 = Pawn.spawnAt(board.getSquareAt(19));
        ChessPiece greenPawn2 = Pawn.spawnAt(board.getSquareAt(4));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(56));

        ArrayList<Square> greenRookActualPossibleMoveLocations = greenRook.generatePossibleMoveLocations();
        ArrayList<Square> orangeRookActualPossibleMoveLocations = orangeRook.generatePossibleMoveLocations();

        assertEquals(greenRookExpectedMoveLocations.length, greenRookActualPossibleMoveLocations.size());
        assertEquals(orangeRookExpectedMoveLocations.length, orangeRookActualPossibleMoveLocations.size());

        for (int i = 0; i < greenRookExpectedMoveLocations.length; i++) {
			actualMoveLocation = greenRookActualPossibleMoveLocations.get(i);
			assertEquals(greenRookExpectedMoveLocations[i],
                         actualMoveLocation.getNumericalLocation());
		}

        for (int i = 0; i < orangeRookExpectedMoveLocations.length; i++) {
			actualMoveLocation = orangeRookActualPossibleMoveLocations.get(i);
			assertEquals(orangeRookExpectedMoveLocations[i],
                         actualMoveLocation.getNumericalLocation());
		}

        logger.log(Level.WARNING, "End testPossibleMoveLocations() - RookTest");
	}
}
