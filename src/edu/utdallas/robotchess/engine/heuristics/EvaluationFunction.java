/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.engine.State;

public interface EvaluationFunction
{
    double f(State state);
}
