/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public abstract class Command
{
    protected int commandID;
    protected int payloadLength;
    protected int robotID;
    
    protected abstract int[] generatePayload();

    protected void setRobotID(int robotID)
    {
        this.robotID = robotID;
    }

    protected int getRobotID()
    {
        return robotID;
    }
}
