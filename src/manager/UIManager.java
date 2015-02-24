/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import java.util.Observer;

import gui.MainFrame;

public class UIManager extends Thread
{
    private ApplicationState applicationState;
    private GameState gameState;
    private RobotState robotState;
    private UIState uiState;
    private MainFrame mainFrame;
    
    protected UIManager(ApplicationState applicationState,
                        UIState uiState,
                        RobotState robotState,
                        GameState gameState)
    {
        this.applicationState = applicationState;
        this.gameState = gameState;
        this.robotState = robotState;
        this.uiState = uiState;
    }

    @Override
    public void run()
    {
        mainFrame = new MainFrame(uiState, robotState);
    }
}
