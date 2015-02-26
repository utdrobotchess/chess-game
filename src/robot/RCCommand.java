/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class RCCommand extends Command
{
    int velocities[];

    public RCCommand(int robotID, int velocities[])
    {
        commandID = 0;
        payloadLength = 0x5;

        this.robotID = robotID;
        this.velocities = velocities;
    }
    
    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        for (int i = 0; i < 4; i++)
            payload[i] = velocities[i];

        payload[4] = 0;

        return payload;
    }
}
