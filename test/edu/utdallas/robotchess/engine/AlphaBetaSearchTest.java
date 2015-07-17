package edu.utdallas.robotchess.engine;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import edu.utdallas.robotchess.game.ChessGame;

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
