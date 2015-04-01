package edu.utdallas.robotchess.engine;

import java.util.List;

public interface Game
{
    double getUtility(State state);
    List<Action> getAvailableActions(State state);
    State computeResult(State state, Action action);
    boolean isGoal(State state);
}
