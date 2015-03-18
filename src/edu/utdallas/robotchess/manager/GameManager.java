/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.manager;

public class GameManager extends Thread
{
    UIState uiState;
    GameState gameState;
    
    protected GameManager(UIState uiState,
                          GameState gameState)
    {
        this.uiState = uiState;
        this.gameState = gameState;
    }

    @Override
    public void run()
    {
        
    }
}
