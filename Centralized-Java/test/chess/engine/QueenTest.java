package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Owner
 */
public class QueenTest {
    
    public QueenTest() {
    }

    @Test
    public void testSpawnAt() {
        ChessPiece greenQueen = Queen.spawnAt(InteriorSquare.generateInteriorSquareAt(3));
        ChessPiece orangeQueen = Queen.spawnAt(InteriorSquare.generateInteriorSquareAt(59));
        
        assertEquals(greenQueen.getIntegerLocation(), 3);
        assertEquals(orangeQueen.getIntegerLocation(), 59);
        
        assertEquals(greenQueen.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(orangeQueen.getTeam(), ChessPiece.Team.ORANGE);
        
        assertEquals(greenQueen.getNumberOfPriorMoves(), 0);
    }
    
    @Test
    public void testPossibleMoveLocations() {
        ChessBoard board = ChessBoard.generateChessBoard();
        ChessPiece greenQueen = Queen.spawnAt(board.getSquareAt(4));
        ChessPiece orangeQueen = Queen.spawnAt(board.getSquareAt(44));
        ChessPiece greenPawn1 = Pawn.spawnAt(board.getSquareAt(0));
        ChessPiece greenPawn2 = Pawn.spawnAt(board.getSquareAt(31));
        ChessPiece orangePawn1 = Pawn.spawnAt(board.getSquareAt(35));
        ChessPiece orangePawn2 = Pawn.spawnAt(board.getSquareAt(41));
        ChessPiece orangePawn3 = Pawn.spawnAt(board.getSquareAt(47));
        
        ArrayList<Square> greenQueenActualPossibleMoveLocations = greenQueen.getPossibleMoveLocations();
        ArrayList<Square> orangeQueenActualPossibleMoveLocations = orangeQueen.getPossibleMoveLocations();
        
        int[] greenQueenExpectedMoveLocations = {1, 2, 3, 5, 6, 7, 11, 12, 13, 18, 20, 22, 25, 28, 32, 36, 44};
        int[] orangeQueenExpectedMoveLocations = {4, 12, 20, 23, 28, 30, 36, 37, 42, 43, 45, 46, 51, 52, 53, 58, 60, 62};
        
        assertEquals(17, greenQueenActualPossibleMoveLocations.size());
        assertEquals(18, orangeQueenActualPossibleMoveLocations.size());
        
        for(int i = 0; i < 17; i++)
            assertEquals(greenQueenExpectedMoveLocations[i], greenQueenActualPossibleMoveLocations.get(i).getLocation());
        
        for(int i = 0; i < 18; i++)
            assertEquals(orangeQueenExpectedMoveLocations[i], orangeQueenActualPossibleMoveLocations.get(i).getLocation());
    }
}
