/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

public class RobotManager
{
    ApplicationState applicationState;
    UIState uiState;
    RobotState robotState;
    
    protected RobotManager(ApplicationState applicationState,
                           UIState uiState,
                           RobotState robotState)
    {
        this.applicationState = applicationState;
        this.uiState = uiState;
        this.robotState = robotState;
    }
}
