package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.*;

public class EvaluationFunction
{
    public double f(ChessGame game)
    {
        MaterialHeuristicFunction material = new MaterialHeuristicFunction();
        double materialValue = material.h(game);
        
        MobilityHeuristicFunction mobility = new MobilityHeuristicFunction();
        double mobiliyValue = mobility.h(game);
        
        return 1.0 * materialValue + 1.0 * mobiliyValue;
    }
}
