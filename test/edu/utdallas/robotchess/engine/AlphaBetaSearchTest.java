package edu.utdallas.robotchess.engine;

import java.util.*;

import org.junit.*;

import edu.utdallas.robotchess.game.*;

public class AlphaBetaSearchTest
{
    @Test
    public void generateMovesTest()
    {
        ChessGame game = new ChessGame();
        AlphaBetaSearch abs = new AlphaBetaSearch();
        ArrayList<Move> moves = abs.generateMoves(game);
        
        Assert.assertEquals(20, moves.size());
    }
}
