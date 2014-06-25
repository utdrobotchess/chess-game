/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class PawnTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

	/*
	 * Ensures that a Pawn spawns correctly, with the proper location assignment,
	 * team, and number of prior moves.
	 */
    @Test
    public void testSpawnAt() {
        logger.log(Level.WARNING, "Begin testSpawnAt() - PawnTest");

        Pawn p1 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(8));
        Pawn p2 = Pawn.spawnAt(InteriorSquare.generateInteriorSquareAt(50));

        assertEquals(p1.getNumericalLocation(), 8);
        assertEquals(p2.getNumericalLocation(), 50);

        assertEquals(p1.getTeam(), Team.GREEN);
        assertEquals(p2.getTeam(), Team.ORANGE);

        assert(p1.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawnAt() - PawnTest");
    }

	/*
	 * Ensures that the opening moves are correctly determined in situations
	 * without any obstructions.
	 */
    @Test
    public void testPossibleOpeningMoves() {
        logger.log(Level.WARNING, "Begin testPossibleOpeningMoves() - PawnTest");

        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn p1 = Pawn.spawnAt(board.getSquareAt(8));
        Pawn p2 = Pawn.spawnAt(board.getSquareAt(54));

        ArrayList<Square> p1ActualPossibleMoveLocations = p1.generatePossibleMoveLocations();
        ArrayList<Square> p2ActualPossibleMoveLocations = p2.generatePossibleMoveLocations();

        assertEquals(2, p1ActualPossibleMoveLocations.size());
        assertEquals(16, p1ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(24, p1ActualPossibleMoveLocations.get(1).getNumericalLocation());
        assertEquals(2, p2ActualPossibleMoveLocations.size());
        assertEquals(38, p2ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(46, p2ActualPossibleMoveLocations.get(1).getNumericalLocation());

        logger.log(Level.WARNING, "End testPossibleOpeningMoves() - PawnTest");
    }

	/*
	 * Ensures that the opening moves are correctly determined in situations
	 * with obstructions either one or two squares away.
	 */
    @Test
    public void testPossibleOpeningMovesWithObstructions() {
        logger.log(Level.WARNING, "Begin testPossibleOpeningMovesWithObstructions() - PawnTest");

        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn p1 = Pawn.spawnAt(board.getSquareAt(10));
        Pawn p2 = Pawn.spawnAt(board.getSquareAt(26));
        Pawn p3 = Pawn.spawnAt(board.getSquareAt(53));
        Pawn p4 = Pawn.spawnAt(board.getSquareAt(45));

        ArrayList<Square> p1ActualPossibleMoveLocations = p1.generatePossibleMoveLocations();
        ArrayList<Square> p3ActualPossibleMoveLocations = p3.generatePossibleMoveLocations();

        assertEquals(1, p1ActualPossibleMoveLocations.size());
        assertEquals(18, p1ActualPossibleMoveLocations.get(0).getNumericalLocation());
        assertEquals(0, p3ActualPossibleMoveLocations.size());

        logger.log(Level.WARNING, "End testPossibleOpeningMovesWithObstructions() - PawnTest");
    }

	/*
	 * Ensures that a pawn could capture a piece from an opposing team that is diagonally in front of it
	 */
    @Test
    public void testPossibleCapturingMoves() {
        logger.log(Level.WARNING, "Begin testPossibleCapturingMoves() - PawnTest");

		//should be able to capture at 17 or move forward to 18 or 26
        int[] greenPawn1ExpectedMoveLocations = {17, 18, 26};

		//should be able to capture at 41 or 42 or move forward to 42 or 34
        int[] orangePawn1ExpectedMoveLocations = {34, 41, 42, 43};

        ChessBoard board = ChessBoard.generateChessBoard();
        Pawn greenPawn1 = Pawn.spawnAt(board.getSquareAt(10));
        Pawn greenPawn2 = Pawn.spawnAt(board.getSquareAt(11));
        Pawn greenPawn3 = Pawn.spawnAt(board.getSquareAt(12));
		Pawn greenPawn4 = Pawn.spawnAt(board.getSquareAt(19));
        Pawn orangePawn1 = Pawn.spawnAt(board.getSquareAt(50));
        Pawn orangePawn2 = Pawn.spawnAt(board.getSquareAt(51));

        orangePawn2.setLocation(board.getSquareAt(17));
        greenPawn2.setLocation(board.getSquareAt(41));
        greenPawn3.setLocation(board.getSquareAt(43));

        ArrayList<Square> greenPawn1ActualPossibleMoveLocations = greenPawn1.generatePossibleMoveLocations();
        ArrayList<Square> orangePawn1ActualPossibleMoveLocations = orangePawn1.generatePossibleMoveLocations();

        assertEquals(greenPawn1ExpectedMoveLocations.length,
                     greenPawn1ActualPossibleMoveLocations.size());
        assertEquals(orangePawn1ExpectedMoveLocations.length,
                     orangePawn1ActualPossibleMoveLocations.size());

		Square actualSquareLocation;

        for (int i = 0; i < greenPawn1ExpectedMoveLocations.length; i++) {
			actualSquareLocation = greenPawn1ActualPossibleMoveLocations.get(i);
            assertEquals(greenPawn1ExpectedMoveLocations[i],
                         actualSquareLocation.getNumericalLocation());
		}

        for (int i = 0; i < orangePawn1ExpectedMoveLocations.length; i++) {
			actualSquareLocation = orangePawn1ActualPossibleMoveLocations.get(i);
			assertEquals(orangePawn1ExpectedMoveLocations[i],
                         actualSquareLocation.getNumericalLocation());
		}

        logger.log(Level.WARNING, "End testPossibleCaptureingMoves() - PawnTest");
    }
}
