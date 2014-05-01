package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessGameTest {
    
    public ChessGameTest() {
    
    }    
    
    public void testBasicPawnOpen() {
        ChessGame game = ChessGame.setupGame();
        ChessBoard board = game.getBoard();
        
        assertEquals(game.getActiveTeam(), ChessPiece.Team.GREEN);
        
        try {
            game.makeMove(51, 35);
            game.makeMove(11, 27);
        } catch (InvalidMoveException ex) {
            System.out.println(ex);
        }
        
        assertEquals(game.getActiveTeam(), ChessPiece.Team.ORANGE);
        assert(board.getSquareAt(27).getOccupant() instanceof Pawn);
    }
}
