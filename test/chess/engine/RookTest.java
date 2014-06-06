package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a Rook, including spawning and determining possible
 * move locations.
 * @author Ryan J. Marcotte
 */
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

        assertEquals(greenRook1.getNumericalLocation(), 0);
        assertEquals(greenRook2.getNumericalLocation(), 7);
        assertEquals(orangeRook1.getNumericalLocation(), 56);
        assertEquals(orangeRook2.getNumericalLocation(), 63);

        assertEquals(greenRook1.getTeam(), Team.GREEN);
        assertEquals(greenRook2.getTeam(), Team.GREEN);
        assertEquals(orangeRook1.getTeam(), Team.ORANGE);
        assertEquals(orangeRook2.getTeam(), Team.ORANGE);

        assert(greenRook1.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawnAt() - RookTest");
    }

	/*
	 * Ensures that a Rook can correctly determine the possible locations to which it can move
	 */
    @Test
    public void testPossibleMoveLocations() {
        logger.log(Level.WARNING, "Begin testPossibleMoveLocations() - RookTest");

        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenRook = Rook.spawnAt(board.getSquareAt(20));
        ChessPiece orangeRook = Rook.spawnAt(board.getSquareAt(60));
        ChessPiece greenPawn1 = Pawn.spawnAt(board.getSquareAt(19));
        ChessPiece greenPawn2 = Pawn.spawnAt(board.getSquareAt(4));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(56));

        ArrayList<Square> greenRookActualPossibleMoveLocations = greenRook.generatePossibleMoveLocations();
        ArrayList<Square> orangeRookActualPossibleMoveLocations = orangeRook.generatePossibleMoveLocations();

        int[] greenRookExpectedMoveLocations = {12, 21, 22, 23, 28, 36, 44, 52, 60};
        int[] orangeRookExpectedMoveLocations = {20, 28, 36, 44, 52, 57, 58, 59, 61, 62, 63};

        assertEquals(9, greenRookActualPossibleMoveLocations.size());
        assertEquals(11, orangeRookActualPossibleMoveLocations.size());

		Square actualMoveLocation;

        for(int i = 0; i < 9; i++) {
			actualMoveLocation = greenRookActualPossibleMoveLocations.get(i);
			assertEquals(greenRookExpectedMoveLocations[i], actualMoveLocation.getNumericalLocation());
		}

        for(int i = 0; i < 11; i++) {
			actualMoveLocation = orangeRookActualPossibleMoveLocations.get(i);
			assertEquals(orangeRookExpectedMoveLocations[i], actualMoveLocation.getNumericalLocation());
		}

        logger.log(Level.WARNING, "End testPossibleMoveLocations() - RookTest");
	}
}
