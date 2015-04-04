package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.ChessGame;

import org.junit.Test;
import org.junit.Assert;

public class MaterialHeuristicFunctionTest
{
    @Test
    public void testMaterialHeuristicFunction()
    {
        ChessGame game = new ChessGame();
        MaterialHeuristicFunction function = new MaterialHeuristicFunction();
        double materialValue = function.h(game);

        Assert.assertEquals(0.0, materialValue, 0.01);
    }
}
