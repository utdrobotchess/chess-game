/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

public class GameManager
{
    ApplicationState applicationState;
    UIState uiState;
    GameState gameState;
    
    protected GameManager(ApplicationState applicationState,
                          UIState uiState,
                          GameState gameState)
    {
        this.applicationState = applicationState;
        this.uiState = uiState;
        this.gameState = gameState;
    }
}
