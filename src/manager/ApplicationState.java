/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

public class ApplicationState
{
    boolean robotsActive;

    public ApplicationState()
    {
        robotsActive = false; 
    }
    
    public synchronized boolean isRobotsActive()
    {
        return robotsActive;
    }
    
    public synchronized void setRobotsActive(boolean robotsActive)
    {
        this.robotsActive = robotsActive;
    }
}
