/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.ChessGame;
import edu.utdallas.robotchess.engine.ChessGameState;

import org.junit.Test;
import org.junit.Assert;

public class MaterialHeuristicFunctionTest
{
    @Test
    public void testMaterialHeuristicFunction()
    {
        ChessGameState gameState = new ChessGameState(new ChessGame());
        MaterialHeuristicFunction function = new MaterialHeuristicFunction();
        double materialValue = function.h(gameState);

        Assert.assertEquals(0.0, materialValue, 0.01);
    }
}
