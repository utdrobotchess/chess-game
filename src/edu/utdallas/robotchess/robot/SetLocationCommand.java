/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.robot;

public class SetLocationCommand extends Command
{
    final int MIN_LOCATION = 0;
    final int MAX_LOCATION = 63;
    private int location;
    
    public SetLocationCommand(int robotID, int location)
    {
        commandID = 0x5;
        payloadLength = 0x2;
        this.location = location;
        this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        if (location > MAX_LOCATION || location < MIN_LOCATION)
            return null;

        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = location;

        return payload;
    }
}
