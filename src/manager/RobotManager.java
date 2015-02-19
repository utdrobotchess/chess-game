/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import robot.MotionPlanner;
import robot.Motion;

public class RobotManager extends Thread
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

    @Override
    public void run()
    {
        MotionPlanner planner = new MotionPlanner(robotState, 8, 8);
        planner.start();

        // TODO insert Communicator thread here

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                
            }
        }
    }
}
