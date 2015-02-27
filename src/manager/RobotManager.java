/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import robot.ChessbotCommunicator;
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

        ChessbotCommunicator communicator = new ChessbotCommunicator(robotState,
                                                                     "/dev/ttyUSB0",
                                                                     57600);
        communicator.run(9);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                
            }
        }
    }
}
