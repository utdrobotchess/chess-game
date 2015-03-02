/**
 *
 * @author Ryan J. Marcotte
 */

package engine;

import java.util.List;

public class ChessGame implements Game
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
