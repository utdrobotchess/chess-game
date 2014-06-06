package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a queen, including generation and determination of
 * possible move locations
 * @author Ryan J. Marcotte
 */
public class QueenTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

    @Test
    public void testSpawnAt() {
        logger.log(Level.WARNING, "Begin testSpawnAt() - QueenTest");

        ChessPiece greenQueen = Queen.spawnAt(InteriorSquare.generateInteriorSquareAt(3));
        ChessPiece orangeQueen = Queen.spawnAt(InteriorSquare.generateInteriorSquareAt(59));

        assertEquals(greenQueen.getNumericalLocation(), 3);
        assertEquals(orangeQueen.getNumericalLocation(), 59);

        assertEquals(greenQueen.getTeam(), Team.GREEN);
        assertEquals(orangeQueen.getTeam(), Team.ORANGE);

        assert(greenQueen.hasNotMoved());

        logger.log(Level.WARNING, "End testSpawnAt() - QueenTest");
    }

    @Test
    public void testPossibleMoveLocations() {
        logger.log(Level.WARNING, "Begin testPossibleMoveLocations() - QueenTest");

        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenQueen = Queen.spawnAt(board.getSquareAt(4));
        ChessPiece orangeQueen = Queen.spawnAt(board.getSquareAt(44));
        ChessPiece greenPawn1 = Pawn.spawnAt(board.getSquareAt(0));
        ChessPiece greenPawn2 = Pawn.spawnAt(board.getSquareAt(31));
        ChessPiece orangePawn1 = Pawn.spawnAt(board.getSquareAt(35));
        ChessPiece orangePawn2 = Pawn.spawnAt(board.getSquareAt(41));
        ChessPiece orangePawn3 = Pawn.spawnAt(board.getSquareAt(47));

        ArrayList<Square> greenQueenActualPossibleMoveLocations = greenQueen.generatePossibleMoveLocations();
        ArrayList<Square> orangeQueenActualPossibleMoveLocations = orangeQueen.generatePossibleMoveLocations();

        int[] greenQueenExpectedMoveLocations = {1, 2, 3, 5, 6, 7, 11, 12, 13, 18, 20, 22, 25, 28, 32, 36, 44};
        int[] orangeQueenExpectedMoveLocations = {4, 12, 20, 23, 28, 30, 36, 37, 42, 43, 45, 46, 51, 52, 53, 58, 60, 62};

        assertEquals(17, greenQueenActualPossibleMoveLocations.size());
        assertEquals(18, orangeQueenActualPossibleMoveLocations.size());

		Square actualLocation;

        for(int i = 0; i < 17; i++) {
			actualLocation = greenQueenActualPossibleMoveLocations.get(i);
		 	assertEquals(greenQueenExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}

        for(int i = 0; i < 18; i++) {
			actualLocation = orangeQueenActualPossibleMoveLocations.get(i);
		 	assertEquals(orangeQueenExpectedMoveLocations[i], actualLocation.getNumericalLocation());
		}

        logger.log(Level.WARNING, "End tesetPossibleMoveLocations() - QueenTest");
    }
}
