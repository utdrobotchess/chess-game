/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.engine;

import java.util.List;

public class Chess implements Game
{
    public double getUtility(State state)
    {
        return 0.0;
    }
    
    public List<Action> getAvailableActions(State state) 
    {
        return null;
    }
    
    public State computeResult(State state, Action action)
    {
        return null;
    }
    
    public boolean isGoal(State state)
    {
        return false;
    }
}
