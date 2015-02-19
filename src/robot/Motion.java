/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class Motion
{
    private int currentRobotPositions[];
    private int desiredRobotPositions[];
    
    public Motion(int current[], int desired[])
    {
        currentRobotPositions = current;
        desiredRobotPositions = desired;
    }

    public int[] getCurrent()
    {
        return currentRobotPositions;
    }

    public int[] getDesired()
    {
        return desiredRobotPositions;
    }
}
