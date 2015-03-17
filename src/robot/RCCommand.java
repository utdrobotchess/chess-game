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
        commandID = 0xB;
        payloadLength = 0x5;
        requiresACK = false;

        this.robotID = robotID;
        this.velocities = velocities;
    }
    
    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        for (int i = 1; i < 5; i++)
            payload[i] = velocities[i];

        return payload;
    }
}
