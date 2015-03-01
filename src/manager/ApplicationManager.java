/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

public class ApplicationManager
{
    UIManager uiManager;
    RobotManager robotManager;
    GameManager gameManager;

    ApplicationState applicationState;
    UIState uiState;
    RobotState robotState;
    GameState gameState;
    
    private ApplicationManager()
    {
        applicationState = new ApplicationState();
        uiState = new UIState();
        robotState = new RobotState();
        gameState = new GameState();
        
        uiManager = new UIManager(applicationState, uiState, robotState, gameState);
        robotManager = new RobotManager(applicationState, uiState, robotState);
        gameManager = new GameManager(uiState, gameState);

        uiManager.start();
        robotManager.start();
        gameManager.start();

    }

    public static void main(String[] args)
    {
        ApplicationManager applicationManager = new ApplicationManager();
    }
}
