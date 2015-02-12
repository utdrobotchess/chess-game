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
        
        uiManager = new UIManager(applicationState, uiState, robotState, gameState);
        robotManager = new RobotManager(applicationState, uiState, robotState);
        gameManager = new GameManager(applicationState, uiState, gameState);
    }

    public static void main(String[] args)
    {
        ApplicationManager applicationManager = new ApplicationManager();
    }
}
