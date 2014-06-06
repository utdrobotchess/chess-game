package chess.engine;

import java.util.ArrayList;
import java.util.logging.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a bishop, including spawning and determining possible move locations
 * @author Ryan J. Marcotte
 */
public class BishopTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

	/*
	 * Ensures that a Bishop is generated correctly, with the proper location, team, and prior move status
	 */
    @Test
    public void testSpawnBishopAt() {
        logger.log(Level.WARNING, "Begin testSpawnBishopAt() - BishopTest");

        ChessPiece greenBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(2));
        ChessPiece greenBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(5));
        ChessPiece orangeBishop1 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(58));
        ChessPiece orangeBishop2 = Bishop.spawnAt(InteriorSquare.generateInteriorSquareAt(61));

        assertEquals(2, greenBishop1.getNumericalLocation());
        assertEquals(5, greenBishop2.getNumericalLocation());
        assertEquals(58, orangeBishop1.getNumericalLocation());
        assertEquals(61, orangeBishop2.getNumericalLocation());

        assertEquals(Team.GREEN, greenBishop1.getTeam());
        assertEquals(Team.GREEN, greenBishop2.getTeam());
        assertEquals(Team.ORANGE, orangeBishop1.getTeam());
        assertEquals(Team.ORANGE, orangeBishop2.getTeam());

        assert(greenBishop1.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawnBishopAt() - BishopTest");
    }

	/*
	 * Ensures that a Bishop can correctly determine the list of moves available to it, including cases of
	 * encountering the edge of the board, a teammate, and an opposing piece
	 */
    @Test
    public void testPossibleMoveLocations() {
        logger.log(Level.WARNING, "Begin testPossibleMoveLocations() - BishopTest");

        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenBishop = Bishop.spawnAt(board.getSquareAt(11));
        ChessPiece orangeBishop = Bishop.spawnAt(board.getSquareAt(47));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(25));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(61));

        ArrayList<Square> greenBishopActualPossibleMoveLocations = greenBishop.generatePossibleMoveLocations();
        ArrayList<Square> orangeBishopActualPossibleMoveLocations = orangeBishop.generatePossibleMoveLocations();

        int[] greenBishopExpectedMoveLocations = {2, 4, 18, 20, 29, 38, 47};
        int[] orangeBishopExpectedMoveLocations = {11, 20, 29, 38, 54};

        assertEquals(7, greenBishopActualPossibleMoveLocations.size());
        assertEquals(5, orangeBishopActualPossibleMoveLocations.size());

		Square actualLocation;

        for(int i = 0; i < 7; i++) {
            actualLocation = greenBishopActualPossibleMoveLocations.get(i);
			assertEquals(greenBishopExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}

        for(int i = 0; i < 5; i++) {
			actualLocation = orangeBishopActualPossibleMoveLocations.get(i);
			assertEquals(orangeBishopExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}

        logger.log(Level.WARNING, "End testPossibleMoveLications() - BishopTest");
    }
}
