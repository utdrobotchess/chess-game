package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.engine.State;

public interface HeuristicFunction
{
    double h(State state);
}
