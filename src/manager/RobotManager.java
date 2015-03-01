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
        while (true) {
            if (applicationState.isRobotsActive()) {
                MotionPlanner planner = new MotionPlanner(robotState, 8, 8);
                planner.run();

                // TODO figure out how to detect the XBEE
                ChessbotCommunicator communicator = new ChessbotCommunicator(robotState,
                                                                     "/dev/ttyUSB0",
                                                                     57600);
                communicator.run(9);

                while (applicationState.isRobotsActive()) {
                    try { Thread.sleep(10); } catch (InterruptedException ex) {}
                }

                communicator.terminate();
                planner.terminate();
            }
            
            try { Thread.sleep(10); } catch (InterruptedException ex) {}
        }
    }
}
