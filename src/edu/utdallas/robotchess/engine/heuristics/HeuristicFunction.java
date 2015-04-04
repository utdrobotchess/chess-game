package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.*;

public interface HeuristicFunction
{
    double h(ChessGame game);
}
