/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.manager;

import edu.utdallas.robotchess.robot.ChessbotCommunicator;
import edu.utdallas.robotchess.robot.MotionPlanner;
import edu.utdallas.robotchess.robot.Motion;

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
                planner.start();

                ChessbotCommunicator communicator = new ChessbotCommunicator(robotState,
                                                                            57600);
                communicator.start();

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
