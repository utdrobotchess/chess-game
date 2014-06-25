/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class KingTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

    @Test
    public void testSpawn() {
        logger.log(Level.WARNING, "Begin testSpawn() - KingTest");

        ChessPiece greenKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(4));
        ChessPiece orangeKing = King.spawnAt(InteriorSquare.generateInteriorSquareAt(60));

        assertEquals(greenKing.getNumericalLocation(), 4);
        assertEquals(orangeKing.getNumericalLocation(), 60);

        assertEquals(greenKing.getTeam(), Team.GREEN);
        assertEquals(orangeKing.getTeam(), Team.ORANGE);

        assert(greenKing.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawn() - KingTest");
    }

    @Test
    public void testPossibleMoveLocations() {
        logger.log(Level.WARNING, "Begin testPossibleMoveLocations() - KingTest");

        int[] greenKingExpectedMoveLocations = {19, 20, 21, 27, 35, 36, 37};
        int[] orangeKingExpectedMoveLocations = {27, 28, 29, 35, 37, 43, 45};

		Square actualLocation;

        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenKing = King.spawnAt(board.getSquareAt(28));
        ChessPiece orangeKing = King.spawnAt(board.getSquareAt(36));
        ChessPiece greenPawn = Pawn.spawnAt(board.getSquareAt(29));
        ChessPiece orangePawn = Pawn.spawnAt(board.getSquareAt(44));

        ArrayList<Square> greenKingActualPossibleMoveLocations = greenKing.generatePossibleMoveLocations();
        ArrayList<Square> orangeKingActualPossibleMoveLocations = orangeKing.generatePossibleMoveLocations();

        assertEquals(greenKingExpectedMoveLocations.length,
                     greenKingActualPossibleMoveLocations.size());
        assertEquals(orangeKingExpectedMoveLocations.length,
                     orangeKingActualPossibleMoveLocations.size());

        for (int i = 0; i < greenKingExpectedMoveLocations.length; i++) {
			actualLocation = greenKingActualPossibleMoveLocations.get(i);
            assertEquals(greenKingExpectedMoveLocations[i],
                         actualLocation.getNumericalLocation());

			actualLocation = orangeKingActualPossibleMoveLocations.get(i);
            assertEquals(orangeKingExpectedMoveLocations[i],
                         actualLocation.getNumericalLocation());
        }

        logger.log(Level.WARNING, "End testPossibleMoveLocations() - KingTest");
    }
}
